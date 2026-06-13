package com.nodlify.observability.audit.infrastructure;

import com.nodlify.observability.audit.application.AuditEventUseCase;
import com.nodlify.observability.audit.domain.Action;
import com.nodlify.observability.audit.domain.ActivityEvent;
import com.nodlify.observability.audit.domain.ActivityPayload;
import com.nodlify.poll.application.PollActivityEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "nodlify.observability.audit.enabled", havingValue = "true")
class PollActivityListener {

    private final AuditEventUseCase auditEventUseCase;
    private final AuditConfig auditConfig;

    @Async
    @EventListener
    @Transactional
    // TODO: refactor, audit module should not be aware of poll module
    public void onPollActivity(PollActivityEvent event) {
        var auditEvent = new ActivityEvent(
                event.userId(),
                Action.of(event.action()),
                auditConfig.isExtractIp() ? event.remoteAddress() : null,
                auditConfig.isExtractUserAgent() ? event.userAgent() : null,
                new ActivityPayload(event.attributes())
        );
        auditEventUseCase.save(auditEvent);
        log.debug("Audit poll activity: {}", event.action());
    }
}
