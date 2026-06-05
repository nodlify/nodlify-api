package com.nodlify.observability.problem.infrastructure;

import com.nodlify.observability.problem.domain.ProblemLog;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

final class ProblemLogSpecifications {

    private ProblemLogSpecifications() {
    }

    static Specification<ProblemLog> titleEquals(String title) {
        return (root, _, criteria) -> (title == null || title.isBlank())
                ? criteria.conjunction()
                : criteria.equal(root.get("title"), title);
    }

    static Specification<ProblemLog> statusEquals(Integer status) {
        return (root, _, criteria) -> (status == null)
                ? criteria.conjunction()
                : criteria.equal(root.get("status"), status);
    }

    static Specification<ProblemLog> detailsEquals(String details) {
        return (root, _, criteria) -> (details == null || details.isBlank())
                ? criteria.conjunction()
                : criteria.equal(root.get("details"), details);
    }

    static Specification<ProblemLog> instanceEquals(String instance) {
        return (root, _, criteria) -> (instance == null || instance.isBlank())
                ? criteria.conjunction()
                : criteria.equal(root.get("instance"), instance);
    }

    static Specification<ProblemLog> createdAtFrom(Instant from) {
        return (root, _, criteria) -> (from == null)
                ? criteria.conjunction()
                : criteria.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    static Specification<ProblemLog> createdAtTo(Instant to) {
        return (root, _, criteria) -> (to == null)
                ? criteria.conjunction()
                : criteria.lessThan(root.get("createdAt"), to);
    }
}
