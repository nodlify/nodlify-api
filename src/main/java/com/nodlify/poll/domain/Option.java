package com.nodlify.poll.domain;

import com.nodlify.shared.domain.Identifier;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;

import static lombok.AccessLevel.PROTECTED;


@Getter
@Entity
@Table(name = "options")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Option {

    @With
    @EmbeddedId
    private Identifier id = Identifier.generate();

    @Embedded
    private TimeRange timeRange;

    private boolean wholeDay;

    public static Option from(TimeRange timeRange) {
        return from(timeRange, false);
    }

    public static Option from(TimeRange timeRange, boolean wholeDay) {
        var option = new Option();
        option.timeRange = timeRange;
        option.wholeDay = wholeDay;
        return option;
    }

    public Instant getStartAt() {
        return timeRange.startAt();
    }

    public Instant getEndAt() {
        return timeRange.endAt();
    }
}
