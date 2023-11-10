package com.tickit.app.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tickit.app.project.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NamedNativeQuery(
        name = Category.QUERY_DELETE_TICKET_ASSOCIATION,
        query = "DELETE FROM ticket_categories WHERE categories_id = ?1")
public class Category implements Serializable {

    public static final String PROPERTY_PROJECT = "project";
    public static final String QUERY_DELETE_TICKET_ASSOCIATION = "Category.deleteTicketAssociation";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    /**
     * Color as HEX
     */
    private String color;

    @JsonIgnore
    @NotNull
    @ManyToOne(targetEntity = Project.class, optional = false)
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime modificationDate;
}
