package com.nodlify.observability.audit.infrastructure;

import com.nodlify.observability.audit.domain.Action;
import com.nodlify.observability.audit.domain.AuditLogEventEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;


final class AuditEventSpecifications {

    private AuditEventSpecifications() {
    }

    static Specification<AuditLogEventEntity> actionEquals(Action action) {
        return (root, _, criteria) -> (action == null || action.getValue() == null || action.getValue().isBlank())
                ? criteria.conjunction()
                : criteria.equal(root.get("action"), action);
    }

    static Specification<AuditLogEventEntity> userIdEquals(String userId) {
        return (root, _, criteria) -> (userId == null || userId.isBlank())
                ? criteria.conjunction()
                : criteria.equal(root.get("userId"), userId);
    }

    static Specification<AuditLogEventEntity> createdAtFrom(Instant from) {
        return (root, _, criteria) -> from == null
                ? criteria.conjunction()
                : criteria.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    static Specification<AuditLogEventEntity> createdAtTo(Instant to) {
        return (root, _, criteria) -> to == null
                ? criteria.conjunction()
                : criteria.lessThan(root.get("createdAt"), to);
    }
}
