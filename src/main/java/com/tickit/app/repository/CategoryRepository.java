package com.tickit.app.repository;

import com.tickit.app.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Jpa repository for {@link Category} entity
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.project.id = ?1")
    List<Category> getCategoriesOfProject(Long projectId);
}
