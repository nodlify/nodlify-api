package com.nodlify.observability.audit.application

import com.nodlify.observability.audit.domain.Action
import com.nodlify.observability.audit.domain.AuditEventRepository
import com.nodlify.observability.audit.domain.AuditLogEventEntity
import com.nodlify.shared.domain.Identifier
import com.nodlify.test.stub.AuditEventRepositoryFake

class AuditEventUseCaseConfig {

    static def repository = new AuditEventRepositoryFake()

    static AuditEventUseCase auditEventUseCase(AuditEventRepository auditEventRepository = repository) {
        return new AuditEventService(auditEventRepository)
    }

    static void setupTestData() {

        def auditId = "6cde39e7-fffa-46dc-98ed-3b462c933632"

        def auditLogEventEntity1 = new AuditLogEventEntity()
                .withId(Identifier.of(auditId))
                .withUserId("user@test.email")
                .withAction(Action.of("login_failed"))

        def auditLogEventEntity2 = new AuditLogEventEntity()
                .withId(Identifier.generate())
                .withUserId("admin@test.email")
                .withAction(Action.of("login_failed"))

        repository.save(auditLogEventEntity1)
        repository.save(auditLogEventEntity2)
    }
}
