package com.nodlify.poll.application;

import com.nodlify.poll.domain.Option;

import java.time.Instant;

import static com.nodlify.shared.domain.Value.valueOrNull;


public record OptionData(
        String optionId,
        Instant startAt,
        Instant endAt,
        boolean wholeDay
) {

    static OptionData from(Option option) {
        return new OptionData(
                valueOrNull(option.getId()),
                option.getStartAt(),
                option.getEndAt(),
                option.isWholeDay()
        );
    }
}
