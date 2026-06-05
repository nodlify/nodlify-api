package com.nodlify.observability.audit.infrastructure;

import com.nodlify.observability.audit.application.AuditEventUseCase;
import com.nodlify.observability.audit.domain.AuditEvent;
import com.nodlify.observability.audit.domain.LoginFailedEvent;
import com.nodlify.observability.audit.domain.LoginSuccessEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "nodlify.observability.audit.enabled", havingValue = "true")
class AuditListener {

    private final AuditEventUseCase auditEventUseCase;
    private final AuditConfig auditConfig;

    @Async
    @EventListener
    @Transactional
    public void onLoginSuccess(AuthenticationSuccessEvent event) {
        var name = event.getAuthentication().getName();
        var ip = shouldExtractIp()
                ? RequestDetailsExtractor.extractIp(event.getAuthentication())
                : null;
        var userAgent = shouldExtractUserAgent()
                ? RequestDetailsExtractor.extractUserAgent(event.getAuthentication())
                : null;
        var domainEvent = new LoginSuccessEvent(name, ip, userAgent);
        auditEventUseCase.save(domainEvent);
        log.debug("Login success for user {}", name);
    }

    @Async
    @EventListener
    @Transactional
    public void onLoginFailure(AbstractAuthenticationFailureEvent event) {
        var name = event.getAuthentication().getName();
        var ip = shouldExtractIp()
                ? RequestDetailsExtractor.extractIp(event.getAuthentication())
                : null;
        var userAgent = shouldExtractUserAgent()
                ? RequestDetailsExtractor.extractUserAgent(event.getAuthentication())
                : null;
        var domainEvent = new LoginFailedEvent(name, ip, userAgent);
        auditEventUseCase.save(domainEvent);
        log.debug("Login failed for user {}", name);
    }

    @Async
    @EventListener
    @Transactional
    public void onAuditLog(AuditEvent auditEvent) {
        auditEventUseCase.save(auditEvent);
        log.debug("Audit: {}", auditEvent);
    }

    private boolean shouldExtractIp() {
        return auditConfig.isExtractIp();
    }

    private boolean shouldExtractUserAgent() {
        return auditConfig.isExtractUserAgent();
    }
}
