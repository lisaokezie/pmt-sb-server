package com.tickit.app.ticket;

import com.tickit.app.category.Category;
import com.tickit.app.category.CategoryService;
import com.tickit.app.project.ProjectUpdateEvent;
import com.tickit.app.repository.TicketRepository;
import com.tickit.app.security.user.User;
import com.tickit.app.security.user.UserService;
import com.tickit.app.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TicketService {
    @NonNull
    private final TicketRepository ticketRepository;
    @NonNull
    private final StatusService statusService;
    @NonNull
    private final ApplicationEventPublisher applicationEventPublisher;
    @NonNull
    private final CategoryService categoryService;
    @NonNull
    private final UserService userService;

    @Autowired
    public TicketService(
            @NonNull TicketRepository ticketRepository,
            @NonNull StatusService statusService,
            @NonNull ApplicationEventPublisher applicationEventPublisher,
            @NonNull CategoryService categoryService,
            @NonNull UserService userService) {
        this.ticketRepository = ticketRepository;
        this.statusService = statusService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    /**
     * Retrieves the ticket with the given id
     *
     * @param id of the ticket to be fetched
     * @return {@link Ticket}
     * @throws TicketNotFoundException if no ticket with the given id was found
     */
    @NonNull
    public Ticket getTicket(@NonNull final Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
    }

    public List<Ticket> getTickets() {
        return ticketRepository.findAll();
    }

    public Ticket createTicket(@NonNull final Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @NonNull
    public Ticket updateTicket(@NonNull final Ticket ticket) {
        final Ticket dbTicket = getTicket(ticket.getId());
        dbTicket.setDescription(ticket.getDescription());
        dbTicket.setTitle(ticket.getTitle());
        dbTicket.setStatus(ticket.getStatus());
        dbTicket.setAssignee(getAssignee(ticket));
        dbTicket.setDueDate(ticket.getDueDate());
        applicationEventPublisher.publishEvent(new ProjectUpdateEvent(dbTicket.getProject().getId()));
        return ticketRepository.save(dbTicket);
    }

    @Nullable
    private User getAssignee(@NonNull final Ticket ticket) {
        return (ticket.getAssignee() == null || ticket.getAssignee().getId() == 0L)
                       ? null : userService.getUserById(ticket.getAssignee().getId());
    }

    @NonNull
    public Ticket updateTicketStatus(@NonNull final Long ticketId, @NonNull final Long statusId) {
        final Ticket dbTicket = getTicket(ticketId);
        dbTicket.setStatus(statusService.getStatus(statusId));
        final var savedTicket = ticketRepository.save(dbTicket);
        applicationEventPublisher.publishEvent(new ProjectUpdateEvent(savedTicket.getProject().getId()));
        return savedTicket;
    }

    public boolean deleteTicket(@NonNull final Long id) {
        final Long projectId = getTicket(id).getProject().getId();
        ticketRepository.deleteById(id);
        applicationEventPublisher.publishEvent(new ProjectUpdateEvent(projectId));
        return true;
    }

    @NonNull
    public Ticket updateAssignedCategories(@NonNull final Long ticketId, @NonNull final CategoryAssignment categoryAssignment) {
        final var ticket = getTicket(ticketId);
        final var categoryIds = categoryAssignment.getCategoryIds();
        if (!categoryIds.isEmpty()) {
            final Set<Category> categories = categoryIds
                                                     .stream()
                                                     .map(categoryService::getCategory)
                                                     .collect(Collectors.toSet());
            ticket.setCategories(categories);
        } else {
            ticket.setCategories(new HashSet<Category>());
        }
        final var savedTicket = ticketRepository.save(ticket);
        applicationEventPublisher.publishEvent(new ProjectUpdateEvent(savedTicket.getProject().getId()));
        return savedTicket;
    }
}
