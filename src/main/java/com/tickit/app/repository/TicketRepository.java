package com.tickit.app.repository;

import com.tickit.app.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa repository for {@link Ticket} entity
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
