package com.tickit.app.status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tickit.app.project.Project;
import com.tickit.app.ticket.Ticket;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Status implements Serializable {

    public static final String PROJECT_PROPERTY = "project";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    private String color;

    private String icon;

    @NotNull
    @JsonIgnore
    @ManyToOne(targetEntity = Project.class, optional = false)
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;

    @JsonIgnore
    @OneToMany(targetEntity = Ticket.class, mappedBy = Ticket.PROPERTY_STATUS, cascade = CascadeType.REMOVE)
    private Set<Ticket> tickets;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime modificationDate;

    public Status(String name, String color, String icon) {
        this.name = name;
        this.color = color;
        this.icon = icon;
    }
}
