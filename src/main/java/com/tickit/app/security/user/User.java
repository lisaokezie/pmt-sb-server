package com.tickit.app.security.user;

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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "username_unique",
                        columnNames = "username"
                ),
                @UniqueConstraint(
                        name = "email_unique",
                        columnNames = "email"
                )
        })
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime modificationDate;

    @JsonIgnore
    @OneToMany(targetEntity = Project.class, mappedBy = Project.OWNER_PROPERTY)
    private Set<Project> projects;

    @JsonIgnore
    @ManyToMany(targetEntity = Project.class, mappedBy = Project.MEMBERS_PROPERTY)
    private Set<Project> collaboratingProjects;

    @JsonIgnore
    @OneToMany(targetEntity = Ticket.class, mappedBy = Ticket.PROPERTY_ASSIGNEE)
    private Set<Ticket> assignedTickets;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
