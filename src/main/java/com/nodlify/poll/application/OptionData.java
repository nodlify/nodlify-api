package com.nodlify.poll.application;

import com.nodlify.poll.domain.Option;
import com.nodlify.poll.domain.TextOption;
import com.nodlify.poll.domain.TimeOption;

import static com.nodlify.shared.domain.Value.valueOrNull;


public sealed interface OptionData permits TimeOptionData, TextOptionData {

    String type();

    String optionId();

    static OptionData from(Option option) {
        if (option instanceof TimeOption time) {
            return new TimeOptionData(
                    "TIME",
                    valueOrNull(time.getId()),
                    time.getStartAt(),
                    time.getEndAt(),
                    time.isWholeDay());
        }
        if (option instanceof TextOption text) {
            return new TextOptionData(
                    "SIMPLE",
                    valueOrNull(text.getId()),
                    valueOrNull(text.getLabel()));
        }
        throw new IllegalStateException("Unknown option type: " + option.getClass());
    }
}
