package com.tickit.app.status;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StatusNotFoundException extends ResponseStatusException {
    public StatusNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "The status with id: " + id + " does not exist");
    }
}