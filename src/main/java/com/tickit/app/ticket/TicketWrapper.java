package com.tickit.app.ticket;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TicketWrapper {

    Set<Ticket> tickets;

    public TicketWrapper(Set<Ticket> tickets) {
        this.tickets = tickets;
    }
}
