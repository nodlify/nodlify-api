package com.nodlify.observability.problem.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;


@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemLogFilter {

    private String title;
    private Integer status;
    private String detail;
    private String instance;
    private Instant from;
    private Instant to;

    public boolean isEmpty() {
        return this.title == null
                && this.status == null
                && this.detail == null
                && this.instance == null
                && this.from == null
                && this.to == null;
    }
}
