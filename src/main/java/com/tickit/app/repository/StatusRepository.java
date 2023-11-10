package com.tickit.app.repository;

import com.tickit.app.status.Status;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa repository for {@link Status} entity
 */
public interface StatusRepository extends JpaRepository<Status, Long> {
}
