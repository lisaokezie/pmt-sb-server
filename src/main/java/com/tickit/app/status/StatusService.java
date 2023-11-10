package com.tickit.app.status;

import com.tickit.app.project.ProjectService;
import com.tickit.app.project.ProjectUpdateEvent;
import com.tickit.app.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Service for managing {@link Status} entity
 */
@Service
public class StatusService {
    @NonNull
    private final StatusRepository statusRepository;
    @NonNull
    private final ProjectService projectService;
    @NonNull
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public StatusService(
            @NonNull StatusRepository statusRepository,
            @NonNull ProjectService projectService,
            @NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.statusRepository = statusRepository;
        this.projectService = projectService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Creates a new status for the given project
     *
     * @param status to be created
     * @return saved status
     */
    @NonNull
    public Status createStatus(@NonNull final Long projectId, @NonNull final Status status) {
        final var project = projectService.getProject(projectId);
        status.setProject(project);
        final var savedStatus = statusRepository.save(status);
        applicationEventPublisher.publishEvent(new ProjectUpdateEvent(project.getId()));
        return savedStatus;
    }

    @NonNull
    public Status getStatus(@NonNull final Long id) {
        return statusRepository.findById(id).orElseThrow(() -> new StatusNotFoundException(id));
    }

    /**
     * Updates the given status
     *
     * @param status to be updated
     * @return saved status
     * @throws RuntimeException if status does not exist
     */
    @NonNull
    public Status updateStatus(@NonNull final Status status) {
        final Status dbStatus = getStatus(status.getId());
        dbStatus.setName(status.getName());
        dbStatus.setIcon(status.getIcon());
        dbStatus.setColor(status.getColor());
        final Status savedStatus = statusRepository.save(dbStatus);
        applicationEventPublisher.publishEvent(new ProjectUpdateEvent(savedStatus.getProject().getId()));
        return savedStatus;
    }

    /**
     * Deletes the status with the given id
     *
     * @param id status id
     * @return {@code true} if deletion was successful
     */
    public boolean deleteStatus(@NonNull final Long id) {
        final Long projectId = getStatus(id).getProject().getId();
        statusRepository.deleteById(id);
        applicationEventPublisher.publishEvent(new ProjectUpdateEvent(projectId));
        return true;
    }
}
