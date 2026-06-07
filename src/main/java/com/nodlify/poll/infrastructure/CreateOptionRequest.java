package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.Option;
import com.nodlify.poll.domain.TextOption;
import com.nodlify.poll.domain.TimeOption;
import com.nodlify.poll.domain.TimeRange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
class CreateOptionRequest {

    // 2025-12-16T17:15:30Z
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
    private Boolean wholeDay = false;
    private String label;

    Option toOption() {
        if (label != null && !label.isBlank()) {
            return TextOption.of(label);
        }
        var range = new TimeRange(startAt.toInstant(), endAt.toInstant());
        return TimeOption.of(range, Boolean.TRUE.equals(wholeDay));
    }
}
