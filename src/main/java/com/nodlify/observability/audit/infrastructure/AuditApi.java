package com.nodlify.observability.audit.infrastructure;

import com.nodlify.observability.audit.application.AuditEventUseCase;
import com.nodlify.observability.audit.application.AuditLogFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

import static com.nodlify.observability.audit.infrastructure.AuditLogMapper.toResponse;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;


@Slf4j
@RestController
@RequestMapping("/api/v1/observability/audit")
@AllArgsConstructor
@Tag(name = "Admin - Audit", description = "Audit event endpoints")
class AuditApi {

    private AuditEventUseCase auditEventUseCase;

    @GetMapping
    public PagedModel<AuditLogResponse> getAudits(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DATE_TIME) Instant to
    ) {
        var auditFilter = new AuditLogFilter(action, userId, from, to);
        log.debug("retrieving audits with filter: {}", auditFilter);
        var page = auditEventUseCase.findAll(auditFilter, pageable)
                .map(AuditLogMapper::toResponse);

        log.debug("retrieved {} audits", page.getTotalElements());
        return new PagedModel<>(page);
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Audit event details")
    @Operation(summary = "Get audit", description = "Gets audit event by identifier")
    public ResponseEntity<AuditLogResponse> getAudit(
            @PathVariable
            @Parameter(name = "id", description = "Audit identifier", example = "a1b2c3d4-1111-2222-3333-444455556666")
            String id
    ) {
        log.debug("retrieving audit with id: {}", id);
        var entity = auditEventUseCase.findById(id);

        return ResponseEntity.ok(toResponse(entity));
    }

    @DeleteMapping
    @ApiResponse(responseCode = "204", description = "All audits deleted")
    @Operation(summary = "Delete all audits", description = "Deletes all audits events")
    public ResponseEntity<Void> deleteAll() {
        auditEventUseCase.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Audit deleted")
    @Operation(summary = "Delete audit", description = "Deletes audit event by identifier")
    public ResponseEntity<Void> delete(
            @PathVariable
            @Parameter(name = "id", description = "Audit identifier", example = "a1b2c3d4-1111-2222-3333-444455556666")
            String id
    ) {
        auditEventUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
