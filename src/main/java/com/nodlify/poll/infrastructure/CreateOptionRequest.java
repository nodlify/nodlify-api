package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.TimeRange;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
class CreateOptionRequest {

    // 2025-12-16T17:15:30Z
    @NotNull
    private OffsetDateTime startAt;

    @NotNull
    private OffsetDateTime endAt;

    private Boolean wholeDay = false;

    TimeRange toRange() {
        return new TimeRange(startAt.toInstant(), endAt.toInstant());
    }

    boolean isWholeDay() {
        return wholeDay;
    }
}
