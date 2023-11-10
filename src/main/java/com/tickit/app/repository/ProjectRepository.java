package com.tickit.app.repository;

import com.tickit.app.project.Project;
import com.tickit.app.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Jpa repository for {@link Project} entity
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT u " +
                   "FROM User u " +
                   "LEFT JOIN u.collaboratingProjects cp " +
                   "LEFT JOIN u.projects po " +
                   "WHERE cp.id = ?1 OR po.id = ?1")
    List<User> getProjectOwnerAndMember(Long id);
}
