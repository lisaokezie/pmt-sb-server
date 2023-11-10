package com.tickit.app.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/status")
public class StatusResource {
    @NonNull
    private final StatusService statusService;

    @Autowired
    public StatusResource(@NonNull StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("{statusId}")
    public Status getStatus(@PathVariable final Long statusId) {
        return statusService.getStatus(statusId);
    }

    @PutMapping("{statusId}")
    public Status updateStatus(@PathVariable final Long statusId, @RequestBody final Status status) {
        status.setId(statusId);
        return statusService.updateStatus(status);
    }

    @DeleteMapping("{statusId}")
    public boolean deleteStatus(@PathVariable final Long statusId) {
        return statusService.deleteStatus(statusId);
    }
}
