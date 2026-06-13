package com.nodlify.observability.audit.domain;

import com.nodlify.shared.functional.Maybe;


public record ActivityEvent(
        String userId,
        Action action,
        String remoteAddress,
        String userAgent,
        AuditPayload auditPayload
) implements AuditEvent {

    @Override
    public Maybe<AuditPayload> payload() {
        return Maybe.from(auditPayload);
    }
}
