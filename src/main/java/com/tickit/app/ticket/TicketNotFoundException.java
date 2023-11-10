package com.tickit.app.ticket;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TicketNotFoundException extends ResponseStatusException {
    public TicketNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "The ticket with id: " + id + " does not exist");
    }
}
