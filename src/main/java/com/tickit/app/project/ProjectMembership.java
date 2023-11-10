package com.tickit.app.project;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class ProjectMembership implements Serializable {
    private List<Long> userIds;
}
