package com.nodlify.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class ProblemDetailsForAdminView implements Problem, HasInvalidParams {

    private String title;
    private Integer status;
    private String detail;
    private String instance;
    private String stackTrace;
    private Instant createdAt;

    @Builder.Default
    private String traceId = UUID.randomUUID().toString();

    @Schema(description = "List of invalid arguments or fields that caused the error",
            requiredMode = NOT_REQUIRED)
    @Builder.Default
    private List<InvalidParam> invalidParams = new ArrayList<>();
}
