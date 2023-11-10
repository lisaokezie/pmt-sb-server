package com.tickit.app.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProjectNotFoundException extends ResponseStatusException {
    public ProjectNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "The project with id: " + id + " does not exist");
    }
}