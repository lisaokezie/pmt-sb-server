package com.tickit.app.project;

import com.tickit.app.category.Category;
import com.tickit.app.category.CategoryService;
import com.tickit.app.category.CategoryWrapper;
import com.tickit.app.security.user.UserWrapper;
import com.tickit.app.status.Status;
import com.tickit.app.status.StatusService;
import com.tickit.app.status.StatusWrapper;
import com.tickit.app.ticket.ProjectTicketWrapper;
import com.tickit.app.ticket.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectResource {
    @NonNull
    private final ProjectService projectService;
    @NonNull
    private final StatusService statusService;
    @NonNull
    private final CategoryService categoryService;

    @Autowired
    public ProjectResource(
            @NonNull final ProjectService projectService,
            @NonNull final StatusService statusService,
            @NonNull CategoryService categoryService) {
        this.projectService = projectService;
        this.statusService = statusService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public Project createProject(@RequestBody final Project project) {
        return projectService.createProject(project);
    }

    @GetMapping("{projectId}")
    public Project getProject(@PathVariable Long projectId) {
        return projectService.getProject(projectId);
    }

    @PutMapping("{projectId}")
    public Project updateProject(@PathVariable Long projectId, @RequestBody final Project project) {
        project.setId(projectId);
        return projectService.updateProject(project);
    }

    @DeleteMapping("{projectId}")
    public boolean deleteProject(@PathVariable Long projectId) {
        return projectService.deleteProject(projectId);
    }

    @PostMapping("{projectId}/status")
    public Status createStatus(@PathVariable Long projectId, @RequestBody final Status status) {
        return statusService.createStatus(projectId, status);
    }

    @GetMapping("{projectId}/status")
    public StatusWrapper getProjectStatuses(@PathVariable Long projectId) {
        return new StatusWrapper(projectService.getProject(projectId).getStatuses());
    }

    @GetMapping("{projectId}/ticket")
    public ProjectTicketWrapper getProjectTickets(@PathVariable Long projectId) {
        return new ProjectTicketWrapper(projectService.getProjectTickets(projectId));
    }

    @GetMapping("{projectId}/users")
    public UserWrapper getProjectMembers(@PathVariable Long projectId) {
        return new UserWrapper(projectService.getProjectMembers(projectId));
    }

    @PostMapping("{projectId}/ticket")
    public Ticket createTicketForProject(@PathVariable Long projectId, @RequestBody Ticket ticket) {
        return projectService.createTicketForProject(projectId, ticket);
    }

    @PutMapping("{projectId}/membership")
    public UserWrapper updateProjectMembership(@PathVariable Long projectId, @RequestBody ProjectMembership projectMembership) {
        return new UserWrapper(projectService.updateProjectMembership(projectId, projectMembership));
    }

    @PostMapping("{projectId}/category")
    public Category createCategory(@PathVariable final Long projectId, @RequestBody final Category category) {
        return categoryService.createCategory(projectId, category);
    }

    @GetMapping("{projectId}/category")
    public CategoryWrapper getCategories(@PathVariable final Long projectId) {
        return new CategoryWrapper(categoryService.getCategoriesOfProject(projectId));
    }
}
