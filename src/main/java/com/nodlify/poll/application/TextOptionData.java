package com.nodlify.poll.application;


public record TextOptionData(
        String type,
        String optionId,
        String label
) implements OptionData {
}
