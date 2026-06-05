package com.nodlify.shared.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

import java.io.Serializable;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;


@Schema(description = "RFC 7807 Problem Details structure")
public interface Problem extends Serializable {

    @Schema(description = "A short, human-readable summary of the problem type",
            example = "Resource Not Found")
    String getTitle();

    @Schema(description = "The HTTP status code for this occurrence of the problem",
            example = "404")
    Integer getStatus();

    @Schema(description = "A human-readable explanation specific to this occurrence of the problem",
            example = "User with ID 123 not found",
            nullable = true,
            requiredMode = NOT_REQUIRED
    )
    @Nullable
    String getDetail();

    @Schema(description = "A URI reference that identifies the specific occurrence of the problem",
            example = "/users/123")
    String getInstance();

    @Schema(description = "A unique identifier for the trace", example = "2abc51a7-0fc7-57d4-b7c1-56a979717a9e")
    String getTraceId();
}
