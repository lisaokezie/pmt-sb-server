package com.tickit.app.status;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class StatusWrapper {

    Set<Status> statuses;

    public StatusWrapper(Set<Status> statuses) {
        this.statuses = statuses;
    }
}
