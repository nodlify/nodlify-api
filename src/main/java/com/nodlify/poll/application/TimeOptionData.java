package com.nodlify.poll.application;

import java.time.Instant;


public record TimeOptionData(
        String type,
        String optionId,
        Instant startAt,
        Instant endAt,
        boolean wholeDay
) implements OptionData {
}
