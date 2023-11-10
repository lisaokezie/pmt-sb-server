package com.tickit.app.repository;

import com.tickit.app.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Jpa repository for {@link User} entity
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = ?1 OR u.email = ?1")
    User findByEmailOrUsername(String credential);
}
