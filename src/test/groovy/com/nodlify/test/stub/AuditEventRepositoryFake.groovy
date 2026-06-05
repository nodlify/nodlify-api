package com.nodlify.test.stub

import com.nodlify.observability.audit.application.AuditLogFilter
import com.nodlify.observability.audit.domain.Action
import com.nodlify.observability.audit.domain.AuditEventRepository
import com.nodlify.observability.audit.domain.AuditLogEventEntity
import com.nodlify.shared.domain.Identifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

import java.util.concurrent.ConcurrentHashMap

class AuditEventRepositoryFake implements AuditEventRepository {

    def map = new ConcurrentHashMap<Identifier, AuditLogEventEntity>()

    @Override
    Page<AuditLogEventEntity> findAll(Pageable pageable) {
        def result = map.values()
        new PageImpl<>(result as List)
    }

    @Override
    Page<AuditLogEventEntity> findAll(AuditLogFilter filter, Pageable pageable) {
        if (filter == null || filter.empty) {
            return findAll(pageable)
        }
        def result = map.values() as List

        if (filter.action())
            result = result.stream().filter { it.action == Action.of(filter.action()) }.toList()

        if (filter.user())
            result = result.stream().filter { it.userId == filter.user() }.toList()

        if (filter.from())
            result = result.stream().filter { it.createdAt <= filter.from() }.toList()

        if (filter.to())
            result = result.stream().filter { it.createdAt > filter.to() }.toList()

        new PageImpl<>(result)
    }

    @Override
    AuditLogEventEntity save(AuditLogEventEntity eventEntity) {
        def id = eventEntity.id ?: Identifier.generate()
        def entityWithId = eventEntity.withId(id)
        map.put(id, entityWithId)
        entityWithId
    }

    @Override
    void deleteById(Identifier identifier) {
        map.remove(identifier)
    }

    @Override
    void deleteAll() {
        map.clear()
    }

    @Override
    Optional<AuditLogEventEntity> findById(Identifier identifier) {
        Optional.ofNullable(map.get(identifier))
    }
}
