package com.pickdate.iam.infrastructure;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;


class AuthenticatedUserRedirectFilter extends OncePerRequestFilter {

    private static final Set<String> ENTRY_PATHS = Set.of("/", "/login", "/register");
    private static final String HOME_PATH = "/home";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (shouldRedirect(request)) {
            response.sendRedirect(HOME_PATH);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldRedirect(HttpServletRequest request) {
        return "GET".equalsIgnoreCase(request.getMethod())
                && ENTRY_PATHS.contains(request.getRequestURI())
                && isAuthenticated(SecurityContextHolder.getContext().getAuthentication());
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
