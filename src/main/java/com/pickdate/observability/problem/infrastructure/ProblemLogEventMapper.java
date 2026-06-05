package com.pickdate.observability.problem.infrastructure;

import com.pickdate.observability.problem.domain.ProblemLog;
import com.pickdate.shared.domain.Identifier;
import com.pickdate.shared.exception.Problem;
import com.pickdate.shared.exception.ProblemCapturedEvent;
import com.pickdate.shared.exception.ProblemDetailsForAdminView;
import com.pickdate.shared.exception.ProblemDetailsWithParameters;


final class ProblemLogEventMapper {

    private ProblemLogEventMapper() {
    }

    static ProblemLog toEntity(ProblemCapturedEvent problemEvent) {
        var problem = problemEvent.problem();
        var entity = new ProblemLog()
                .withId(Identifier.of(problem.getTraceId()))
                .withStatus(problem.getStatus())
                .withDetail(problem.getDetail())
                .withInstance(problem.getInstance())
                .withStackTrace(problemEvent.stackTrace())
                .withTitle(problem.getTitle());

        if (problem instanceof ProblemDetailsWithParameters params) {
            return entity.withInvalidParams(params.getInvalidParams());
        }

        return entity;
    }

    static Problem toProblem(ProblemLog entity) {
        return ProblemDetailsForAdminView.builder()
                .title(entity.getTitle())
                .status(entity.getStatus())
                .detail(entity.getDetail())
                .instance(entity.getInstance())
                .traceId(entity.getId().getValue())
                .stackTrace(entity.getStackTrace())
                .invalidParams(entity.getInvalidParams())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

