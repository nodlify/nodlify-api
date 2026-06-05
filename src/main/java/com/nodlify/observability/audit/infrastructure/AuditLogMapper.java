package com.nodlify.observability.audit.infrastructure;

import com.nodlify.observability.audit.domain.AuditLogEventEntity;

import static com.nodlify.shared.domain.Value.valueOrNull;


final class AuditLogMapper {

    private AuditLogMapper() {
    }

    static AuditLogResponse toResponse(AuditLogEventEntity auditLog) {
        return new AuditLogResponse(
                valueOrNull(auditLog.getId()),
                valueOrNull(auditLog.getAction()),
                auditLog.getUserId(),
                auditLog.getRemoteAddress(),
                auditLog.getUserAgent(),
                auditLog.getPayload(),
                auditLog.getCreatedAt()
        );
    }
}
