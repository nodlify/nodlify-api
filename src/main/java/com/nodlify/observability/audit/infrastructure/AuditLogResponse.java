package com.nodlify.observability.audit.infrastructure;

import com.nodlify.observability.audit.domain.AuditPayload;

import java.time.Instant;

record AuditLogResponse(
        String id,
        String action,
        String user,
        String remoteAddress,
        String userAgent,
        AuditPayload payload,
        Instant createdAt
) {
}
