package com.nodlify.observability.problem.infrastructure;

import com.nodlify.observability.problem.application.ProblemLogFilter;
import com.nodlify.observability.problem.application.ProblemLogUseCase;
import com.nodlify.shared.exception.Problem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;


@RestController
@RequestMapping("/api/v1/observability/problems")
@AllArgsConstructor
@Tag(name = "Admin - Problems", description = "Problem and error reporting endpoints")
class ProblemLogApi {

    private final ProblemLogUseCase problemLogUseCase;

    @GetMapping
    @ApiResponse(responseCode = "200", description = "Successful list of problems")
    @Operation(summary = "List problems", description = "Lists application problems and error reports")
    public Page<Problem> getProblems(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String detail,
            @RequestParam(required = false) String instance,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) Instant to
    ) {

        var problemLogFilter = new ProblemLogFilter()
                .withTitle(title)
                .withStatus(status)
                .withDetail(detail)
                .withInstance(instance)
                .withFrom(from)
                .withTo(to);

        return problemLogUseCase.getProblems(problemLogFilter, pageable)
                .map(ProblemLogEventMapper::toProblem);
    }

    @GetMapping("/{problemId}")
    @Operation(summary = "Get problem by ID", description = "Retrieves a problem by its ID")
    public ResponseEntity<Problem> getProblemById(@PathVariable String problemId) {
        return problemLogUseCase.getProblemById(problemId)
                .map(ProblemLogEventMapper::toProblem)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{problemId}")
    @Operation(summary = "Delete problem", description = "Deletes a problem by its ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProblem(@PathVariable String problemId) {
        problemLogUseCase.deleteProblem(problemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete all", description = "Deletes all application problems and error reports")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAllProblems() {
        problemLogUseCase.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
