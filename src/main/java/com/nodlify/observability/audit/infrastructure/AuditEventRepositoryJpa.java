package com.nodlify.observability.audit.infrastructure;

import com.nodlify.observability.audit.application.AuditLogFilter;
import com.nodlify.observability.audit.domain.Action;
import com.nodlify.observability.audit.domain.AuditEventRepository;
import com.nodlify.observability.audit.domain.AuditLogEventEntity;
import com.nodlify.shared.domain.Identifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import static com.nodlify.observability.audit.infrastructure.AuditEventSpecifications.*;


@Repository
interface AuditEventRepositoryJpa extends AuditEventRepository,
        JpaRepository<AuditLogEventEntity, Identifier>,
        JpaSpecificationExecutor<AuditLogEventEntity> {

    @Override
    default Page<AuditLogEventEntity> findAll(AuditLogFilter filter, Pageable pageable) {
        if (filter == null || filter.isEmpty()) return findAll(pageable);
        var action = Action.ofNullable(filter.action());
        Specification<AuditLogEventEntity> specification = Specification
                .where(actionEquals(action))
                .and(userIdEquals(filter.user()))
                .and(createdAtFrom(filter.from()))
                .and(createdAtTo(filter.to()));

        return findAll(specification, pageable);
    }
}
