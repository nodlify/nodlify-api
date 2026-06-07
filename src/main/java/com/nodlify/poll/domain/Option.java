package com.nodlify.poll.domain;

import com.nodlify.shared.domain.Identifier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;


@Getter
@Entity
@Table(name = "options")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = PROTECTED)
public abstract class Option {

    @EmbeddedId
    private Identifier id = Identifier.generate();

    public Option withId(Identifier id) {
        this.id = id;
        return this;
    }

    public static Option from(TimeRange timeRange) {
        return TimeOption.of(timeRange);
    }

    public static Option from(TimeRange timeRange, boolean wholeDay) {
        return TimeOption.of(timeRange, wholeDay);
    }
}
