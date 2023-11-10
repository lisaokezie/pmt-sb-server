package com.tickit.app.category;

import com.tickit.app.project.ProjectService;
import com.tickit.app.project.ProjectUpdateEvent;
import com.tickit.app.repository.CategoryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service to manage {@link Category} entity.
 */
@Service
public class CategoryService {
    @NonNull
    private final CategoryRepository categoryRepository;
    @NonNull
    private final ProjectService projectService;
    @NonNull
    private final EntityManager entityManager;
    @NonNull
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public CategoryService(
            @NonNull final CategoryRepository categoryRepository,
            @NonNull ProjectService projectService,
            @NonNull final EntityManager entityManager,
            @NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.categoryRepository = categoryRepository;
        this.projectService = projectService;
        this.entityManager = entityManager;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Retrieves the category with the given id
     *
     * @param id of the category to be fetched
     * @return {@link Category}^
     * @throws CategoryNotFoundException if no category with the given id was found
     */
    @NonNull
    public Category getCategory(@NonNull final Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    /**
     * Creates the given category
     *
     * @param category to be created
     * @return saved category
     */
    @NonNull
    public Category createCategory(@NonNull final Long projectId, @NonNull final Category category) {
        category.setProject(projectService.getProject(projectId));
        return categoryRepository.save(category);
    }

    /**
     * Updates the given category
     *
     * @param category to be updated
     * @return saved category
     */
    @NonNull
    public Category updateCategory(@NonNull final Category category) {
        final var dbCategory = getCategory(category.getId());
        dbCategory.setName(category.getName());
        dbCategory.setColor(category.getColor());
        return categoryRepository.save(dbCategory);
    }

    /**
     * Deletes the category with the given id
     *
     * @param id category id
     * @return {@code true} if the deletion was successful
     */
    @Transactional
    public boolean deleteCategory(@NonNull final Long id) {
        final Long projectId = getCategory(id).getProject().getId();
        entityManager.joinTransaction();
        final var query = entityManager.createNamedQuery(Category.QUERY_DELETE_TICKET_ASSOCIATION)
                                  .setParameter(1, id);

        query.executeUpdate();
        categoryRepository.deleteById(id);
        applicationEventPublisher.publishEvent(new ProjectUpdateEvent(projectId));
        return true;
    }

    /**
     * Retrieves the categories which belong to the given project
     *
     * @param projectId project whose categories shall be fetched
     * @return list of categories
     */
    public List<Category> getCategoriesOfProject(@NonNull final Long projectId) {
        return categoryRepository.getCategoriesOfProject(projectId);
    }
}
