package com.nodlify.test.fixture

import com.nodlify.observability.audit.domain.Action
import com.nodlify.observability.audit.domain.AuditLogEventEntity
import com.nodlify.shared.domain.Identifier

import java.time.Instant

class AuditFixture {

    static AuditLogEventEntity audit(String action, String userId, String createdAt) {
        return new AuditLogEventEntity(
                Identifier.generate(),
                Action.of(action),
                userId,
                null,
                null,
                null,
                Instant.parse(createdAt)
        )
    }
}

