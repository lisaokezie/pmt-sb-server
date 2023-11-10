package com.tickit.app.ticket;

import com.tickit.app.status.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class StatusTicketDto implements Serializable, Comparable<StatusTicketDto> {
    private Status status;

    private List<Ticket> tickets;

    public StatusTicketDto(Status status, List<Ticket> tickets) {
        this.status = status;
        this.tickets = tickets;
    }

    @Override
    public int compareTo(StatusTicketDto o) {
        return Long.compare(getStatus().getId(), o.getStatus().getId());
    }
}
