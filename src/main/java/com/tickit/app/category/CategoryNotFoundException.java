package com.tickit.app.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CategoryNotFoundException extends ResponseStatusException {
    public CategoryNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "The category with id: " + id + " does not exist");
    }
}
