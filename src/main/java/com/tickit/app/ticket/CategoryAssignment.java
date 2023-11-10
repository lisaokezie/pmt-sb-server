package com.tickit.app.ticket;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CategoryAssignment {
    private Set<Long> categoryIds;
}
