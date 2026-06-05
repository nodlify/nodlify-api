package com.nodlify.bootstrap.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Optional;


@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider", auditorAwareRef = "auditorProvider")
class AuditionConfig {

    @Bean(name = "auditingDateTimeProvider")
    DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(Instant.now());
    }

    @Bean(name = "auditorProvider")
    AuditorAware<String> auditorAware() {
        return () -> {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return Optional.empty();
            }
            return Optional.ofNullable(auth.getName());
        };
    }
}
