package com.tickit.app.project;

import com.tickit.app.repository.ProjectRepository;
import com.tickit.app.repository.StatusRepository;
import com.tickit.app.security.authentication.AuthenticationService;
import com.tickit.app.security.user.User;
import com.tickit.app.security.user.UserService;
import com.tickit.app.status.Status;
import com.tickit.app.status.StatusNotFoundException;
import com.tickit.app.ticket.StatusTicketDto;
import com.tickit.app.ticket.Ticket;
import com.tickit.app.ticket.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for managing {@link Project} entities
 */
@Service
public class ProjectService {
    private static final List<Status> DEFAULT_STATUSES = List.of(
            new Status("Offen", "#ffa726", "emoji_objects"),
            new Status("In Arbeit", "#66bb6a", "rotate_right"),
            new Status("Erledigt", "#7e57c2", "task_alt")
    );

    @NonNull
    private final ProjectRepository projectRepository;
    @NonNull
    private final StatusRepository statusRepository;
    @NonNull
    private final AuthenticationService authenticationService;
    @NonNull
    private final TicketService ticketService;
    @NonNull
    private final UserService userService;
    @NonNull
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public ProjectService(
            @NonNull final ProjectRepository projectRepository,
            @NonNull final StatusRepository statusRepository,
            @NonNull final AuthenticationService authenticationService,
            @Lazy @NonNull TicketService ticketService,
            @NonNull UserService userService,
            @NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.projectRepository = projectRepository;
        this.authenticationService = authenticationService;
        this.statusRepository = statusRepository;
        this.ticketService = ticketService;
        this.userService = userService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Retrieves the project with the given id
     *
     * @param id of the project to be fetched
     * @return {@link Project}
     * @throws ProjectNotFoundException if no project with the given id was found
     */
    @NonNull
    public Project getProject(@NonNull final Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
    }

    /**
     * Initializes default statuses and saves the given project
     *
     * @param project to be created
     * @return saved project
     */
    @NonNull
    public Project createProject(@NonNull final Project project) {
        project.setOwner(authenticationService.getCurrentUser());
        final var savedProject = projectRepository.save(project);
        initializeDefaultStatuses(savedProject);
        return getProject(project.getId());
    }

    /**
     * Creates default statuses specified in {@link #DEFAULT_STATUSES} for the given project.
     *
     * @param savedProject project to create default statuses for
     */
    private void initializeDefaultStatuses(Project savedProject) {
        DEFAULT_STATUSES.forEach(status -> {
            final var newStatus = new Status();
            newStatus.setIcon(status.getIcon());
            newStatus.setName(status.getName());
            newStatus.setColor(status.getColor());
            newStatus.setProject(savedProject);
            this.statusRepository.save(newStatus);
        });
    }

    /**
     * Updates the given project
     *
     * @param project to be updated
     * @return saved project
     */
    @NonNull
    public Project updateProject(@NonNull final Project project) {
        final var dbProject = getProject(project.getId());
        dbProject.setName(project.getName());
        dbProject.setDescription(project.getDescription());
        final Project savedProject = projectRepository.save(dbProject);
        applicationEventPublisher.publishEvent(new ProjectUpdateEvent(dbProject.getId()));
        return savedProject;
    }

    /**
     * Deletes the project with the given id
     *
     * @param id project id
     * @return {@code true} if deletion was successful
     */
    public boolean deleteProject(@NonNull final Long id) {
        checkProjectOwnership(id);
        projectRepository.deleteById(id);
        return true;
    }

    /**
     * Checks if the currently authenticated user is owner of the project.
     *
     * @param id project to be checked
     * @throws AccessDeniedException if project does not belong to the current user
     */
    private void checkProjectOwnership(Long id) {
        final var projectOwner = getProject(id).getOwner();
        if (authenticationService.getCurrentUser() == null || projectOwner.getId() != authenticationService.getCurrentUser().getId()) {
            throw new AccessDeniedException("User " + projectOwner.getId() + " is not owner of the project");
        }
    }

    public List<StatusTicketDto> getProjectTickets(Long id) {
        final List<StatusTicketDto> statusTicketDtos = new ArrayList<>();
        final var statuses = statusRepository.findAll().stream().filter(status -> status.getProject().getId() == id);
        final var tickets = ticketService.getTickets();
        statuses.forEach(status -> {
            final List<Ticket> ticketsInStatus = tickets.stream()
                                                         .filter(ticket -> ticket.getStatus().getId() == status.getId())
                                                         .sorted().collect(Collectors.toList());
            final var dto = new StatusTicketDto(status, ticketsInStatus);
            statusTicketDtos.add(dto);
        });
        Collections.sort(statusTicketDtos);
        return statusTicketDtos;
    }

    @NonNull
    public Ticket createTicketForProject(Long projectId, Ticket ticket) {
        final Long statusId = ticket.getStatus().getId();
        if (statusId == 0L) {
            throw new IllegalArgumentException("Ticket status must be set");
        }
        ticket.setStatus(statusRepository.findById(statusId).orElseThrow(() -> new StatusNotFoundException(statusId)));
        ticket.setProject(getProject(projectId));
        final Ticket savedTicket = ticketService.createTicket(ticket);
        applicationEventPublisher.publishEvent(new ProjectUpdateEvent(projectId));
        return savedTicket;
    }

    @NonNull
    public List<User> updateProjectMembership(Long projectId, ProjectMembership projectMembership) {
        final Project project = getProject(projectId);
        final Set<User> users = projectMembership.getUserIds()
                                        .stream()
                                        .map(userService::getUserById)
                                        .collect(Collectors.toSet());
        project.setMembers(users);
        final Project savedProject = updateProject(project);
        applicationEventPublisher.publishEvent(new ProjectUpdateEvent(projectId));
        return getProjectMembers(savedProject.getId());
    }

    @NonNull
    public List<User> getProjectMembers(Long projectId) {
        return projectRepository.getProjectOwnerAndMember(projectId);
    }
}
