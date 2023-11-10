package com.tickit.app.security.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {
    public UserNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "The user with id: " + id + " does not exist");
    }
}