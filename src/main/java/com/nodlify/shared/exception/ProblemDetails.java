package com.nodlify.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class ProblemDetails implements Problem {

    private String title;
    private Integer status;
    private String detail;
    private String instance;

    @Builder.Default
    private String traceId = UUID.randomUUID().toString();
}
