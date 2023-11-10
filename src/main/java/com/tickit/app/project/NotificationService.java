package com.tickit.app.project;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements ApplicationListener<ProjectUpdateEvent> {

    @NonNull
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(@NonNull final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Send notification for project update
     *
     * @param event indicating that project was updated
     */
    @Override
    public void onApplicationEvent(@NonNull final ProjectUpdateEvent event) {
        final String message = "Project with id " + event.getProjectId() + " was updated";
        final String destination = "/topic/project/" + event.getProjectId();
        messagingTemplate.convertAndSend(destination, message);
    }
}
