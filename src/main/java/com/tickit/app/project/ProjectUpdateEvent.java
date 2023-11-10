package com.tickit.app.project;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ProjectUpdateEvent extends ApplicationEvent {
    final Long projectId;

    public ProjectUpdateEvent(Long projectId) {
        super(projectId);
        this.projectId = projectId;
    }
}
