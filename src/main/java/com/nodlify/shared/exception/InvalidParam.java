package com.nodlify.shared.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvalidParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "The name of the invalid parameter", example = "email")
    private String name;

    @Schema(
            description = "The provided invalid value (string or number)",
            oneOf = {String.class, Number.class, Boolean.class},
            example = "test@test.test"
    )
    private Object value;

    @Schema(description = "Reason why the value is invalid", example = "Must be a valid email address")
    private String reason;
}
