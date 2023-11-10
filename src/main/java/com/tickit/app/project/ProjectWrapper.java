package com.tickit.app.project;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ProjectWrapper {
    Set<Project> projects;

    public ProjectWrapper(Set<Project> projects) {
        this.projects = projects;
    }
}
