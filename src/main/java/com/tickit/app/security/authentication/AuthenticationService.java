package com.tickit.app.security.authentication;

import com.tickit.app.security.user.User;
import com.tickit.app.security.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private static final String HEADER = "Authentication";

    private static final String TOKEN_PREFIX = "jwt";


    @NonNull
    private final AuthenticationManager authenticationManager;
    @NonNull
    private final UserService userService;
    @NonNull
    private final JwtService jwtService;

    @Autowired
    public AuthenticationService(
            @NonNull AuthenticationManager authenticationManager,
            @NonNull UserService userService,
            @NonNull JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @NonNull
    public User authenticateUser(LoginCredentials loginCredentials, @NonNull HttpServletResponse response) {
        if (loginCredentials.getUsername() == null || loginCredentials.getPassword() == null) {
            throw new IllegalArgumentException("username and password must not be null");
        }

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginCredentials.getUsername(), loginCredentials.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final var user = (User) authentication.getPrincipal();
        response.addCookie(jwtService.generateJwtCookie(user));
        return user;
    }

    @NonNull
    public User registerUser(User user) {
        return userService.createUser(user);
    }

    @Nullable
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
