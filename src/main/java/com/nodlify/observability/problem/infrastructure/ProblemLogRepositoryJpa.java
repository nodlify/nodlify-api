package com.nodlify.observability.problem.infrastructure;

import com.nodlify.observability.problem.application.ProblemLogFilter;
import com.nodlify.observability.problem.domain.ProblemLog;
import com.nodlify.observability.problem.domain.ProblemLogRepository;
import com.nodlify.shared.domain.Identifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import static com.nodlify.observability.problem.infrastructure.ProblemLogSpecifications.*;


@Repository
interface ProblemLogRepositoryJpa extends ProblemLogRepository,
        JpaRepository<ProblemLog, Identifier>,
        JpaSpecificationExecutor<ProblemLog> {

    @Override
    default Page<ProblemLog> findAll(ProblemLogFilter filter, Pageable pageable) {
        if (filter == null || filter.isEmpty()) {
            return findAll(pageable);
        }

        Specification<ProblemLog> specification = Specification
                .where(titleEquals(filter.getTitle()))
                .and(statusEquals(filter.getStatus()))
                .and(detailsEquals(filter.getDetail()))
                .and(instanceEquals(filter.getInstance()))
                .and(createdAtFrom(filter.getFrom()))
                .and(createdAtTo(filter.getTo()));

        return findAll(specification, pageable);
    }
}
