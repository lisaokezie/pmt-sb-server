package com.tickit.app.security.user;

import com.tickit.app.project.Project;
import com.tickit.app.repository.UserRepository;
import com.tickit.app.ticket.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service to manage {@link User} entity. Implements {@link UserDetailsService} as required by Spring Security to
 * perform authentication.
 */
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the user with the given id
     *
     * @param id of the user to be fetched
     * @return {@link User} if found
     * @throws UserNotFoundException if user does not exist
     */
    @NonNull
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Retrieves all users
     *
     * @return List of users
     */
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates the given user by merging editable properties into persisted user.
     *
     * @param user to be updated
     * @return saved user
     */
    @NonNull
    public User updateUser(final User user) {
        final User dbUser = getUserById(user.getId());
        // ensure only username, name, surname and email address can be edited
        dbUser.setName(user.getName());
        dbUser.setSurname(user.getSurname());
        dbUser.setName(user.getName());
        dbUser.setUsername(user.getUsername());
        return userRepository.save(dbUser);
    }

    /**
     * Creates a new user and encodes the provided password.
     *
     * @param user to be created
     * @return saved user
     */
    @NonNull
    public User createUser(User user) {
        user.setPassword(encodePassword(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Used by spring security to authenticate user by username or email address
     *
     * @param username the username identifying the user whose data is required.
     * @return User for authentication
     * @throws UsernameNotFoundException if user could not be identified by email or username
     */
    @Override
    @NonNull
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    /**
     * Changes the user's password if the correct old password was provided.
     *
     * @param passwordChangeRequest request containing old and new password
     * @param userId                user whose password shall be changed
     * @return {@code true} if the change was successful
     */
    public boolean changeUserPassword(@NonNull final PasswordChangeRequest passwordChangeRequest, final Long userId) {
        final String newPassword = passwordChangeRequest.getNewPassword();
        final String oldPassword = passwordChangeRequest.getOldPassword();
        if (newPassword == null || oldPassword == null) {
            throw new IllegalArgumentException("Old and new password must be provided");
        }
        // compare old password with persisted user password
        final User user = getUserById(userId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("The entered password is invalid");
        }
        user.setPassword(encodePassword(newPassword));
        userRepository.save(user);
        return true;
    }

    /**
     * Encodes the given password using the {@link BCryptPasswordEncoder}.
     *
     * @return encoded password
     */
    private String encodePassword(@NonNull final String password) {
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * Retrieves the projects the given user owns or belongs to
     *
     * @param userId user whose projects shall be fetched
     * @return set of projects
     */
    @NonNull
    public Set<Project> getUserProjects(Long userId) {
        final var user = getUserById(userId);
        Set<Project> projects = new HashSet<>();
        projects.addAll(user.getCollaboratingProjects());
        projects.addAll(user.getProjects());
        return projects;
    }

    /**
     * Retrieves the tickets which are assigned to the given user
     *
     * @param userId user whose assigned tickets shall be fetched
     * @return set of tickets
     */
    @NonNull
    public Set<Ticket> getUserTickets(Long userId) {
        final var user = getUserById(userId);
        return user.getAssignedTickets();
    }
}
