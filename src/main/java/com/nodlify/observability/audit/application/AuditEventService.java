package com.nodlify.observability.audit.application;

import com.nodlify.observability.audit.domain.AuditEvent;
import com.nodlify.observability.audit.domain.AuditEventRepository;
import com.nodlify.observability.audit.domain.AuditLogEventEntity;
import com.nodlify.shared.domain.Identifier;
import com.nodlify.shared.domain.Property;
import com.nodlify.shared.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Transactional
@CacheConfig(cacheNames = "activities")
class AuditEventService implements AuditEventUseCase {

    private final AuditEventRepository repository;

    @Transactional(readOnly = true)
    @Cacheable(key = "#id")
    public AuditLogEventEntity findById(String id) {
        return getAuditEventEntity(id);
    }

    @Transactional(readOnly = true)
    @Cacheable
    public Page<AuditLogEventEntity> findAll(AuditLogFilter auditLogFilter, Pageable pageable) {
        return repository.findAll(auditLogFilter, pageable);
    }

    @Transactional(readOnly = true)
    @Cacheable
    public Page<AuditLogEventEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @CacheEvict(allEntries = true)
    public void save(AuditEvent auditEvent) {
        var eventEntity = AuditEventFactory.createFrom(auditEvent);
        repository.save(eventEntity);
    }

    @CacheEvict(allEntries = true)
    public void delete(String id) {
        repository.deleteById(Identifier.of(id));
    }

    @CacheEvict(allEntries = true)
    public void deleteAll() {
        repository.deleteAll();
    }

    private AuditLogEventEntity getAuditEventEntity(String id) {
        return repository.findById(Identifier.of(id))
                .orElseThrow(() -> new NotFoundException(Property.of("id", id), "Audit with id %s not found".formatted(id)));
    }
}
