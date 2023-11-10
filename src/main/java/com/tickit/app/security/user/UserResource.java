package com.tickit.app.security.user;

import com.tickit.app.project.ProjectWrapper;
import com.tickit.app.ticket.TicketWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/security/user")
public class UserResource {

    @NonNull
    private final UserService userService;

    @Autowired
    public UserResource(@NonNull final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserWrapper getUsers() {
        return new UserWrapper(userService.getUsers());
    }

    @GetMapping("{userId}")
    public User getUser(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("{userId}")
    public User updateUser(@PathVariable String userId, @RequestBody User user) {
        user.setId(Long.parseLong(userId));
        return userService.updateUser(user);
    }

    @PostMapping("{userId}/passwordChange")
    public boolean changePassword(
            @RequestBody PasswordChangeRequest passwordChangeRequest,
            @PathVariable Long userId) {
        return userService.changeUserPassword(passwordChangeRequest, userId);
    }

    @GetMapping("{userId}/projects")
    public ProjectWrapper getUserProjects(@PathVariable Long userId) {
        return new ProjectWrapper(userService.getUserProjects(userId));
    }

    @GetMapping("{userId}/tickets")
    public TicketWrapper getUserTickets(@PathVariable Long userId) {
        return new TicketWrapper(userService.getUserTickets(userId));
    }
}
