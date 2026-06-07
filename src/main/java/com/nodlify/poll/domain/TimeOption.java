package com.nodlify.poll.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static lombok.AccessLevel.PROTECTED;


@Getter
@Entity
@Table(name = "time_options")
@NoArgsConstructor(access = PROTECTED)
public class TimeOption extends Option {

    @Embedded
    private TimeRange timeRange;

    private boolean wholeDay;

    private TimeOption(TimeRange timeRange, boolean wholeDay) {
        this.timeRange = timeRange;
        this.wholeDay = wholeDay;
    }

    public static TimeOption of(TimeRange timeRange) {
        return new TimeOption(timeRange, false);
    }

    public static TimeOption of(TimeRange timeRange, boolean wholeDay) {
        return new TimeOption(timeRange, wholeDay);
    }

    public Instant getStartAt() {
        return timeRange.startAt();
    }

    public Instant getEndAt() {
        return timeRange.endAt();
    }
}
