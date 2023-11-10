package com.tickit.app.security.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter used to authenticate user via JWT provided by cookie
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
    @NonNull
    private final JwtService jwtService;

    @Autowired
    public JwtFilter(@NonNull JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final var token = jwtService.getJwtCookieFromRequest(request);
        // proceed with filter chain if no valid cookie was found
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
    }

    /**
     * Sets the security context with the authenticated user provided by the given JWT
     *
     * @param token   used for authentication
     * @param request containing authentication details
     */
    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = jwtService.getUserDetailsFromJwt(token);

        final UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

