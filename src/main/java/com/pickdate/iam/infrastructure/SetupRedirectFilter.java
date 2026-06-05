package com.pickdate.iam.infrastructure;

import com.pickdate.iam.application.ApplicationSetupUseCase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
class SetupRedirectFilter extends OncePerRequestFilter {

    private final ApplicationSetupUseCase applicationSetupUseCase;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        boolean isRootPath = "GET".equalsIgnoreCase(request.getMethod())
                && "/".equals(request.getRequestURI());

        if (isRootPath && !applicationSetupUseCase.setupCompleted()) {
            response.sendRedirect("/setup");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
