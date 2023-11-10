package com.tickit.app.ticket;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectTicketWrapper {

    List<StatusTicketDto> statusTicketMap;

    public ProjectTicketWrapper(List<StatusTicketDto> statusTicketMap) {
        this.statusTicketMap = statusTicketMap;
    }
}