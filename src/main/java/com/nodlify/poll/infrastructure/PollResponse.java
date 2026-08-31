package com.nodlify.poll.infrastructure;

import com.nodlify.poll.application.LocationData;
import com.nodlify.poll.application.OptionData;
import com.nodlify.poll.application.ParticipantData;
import com.nodlify.poll.application.PollData;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;


record PollResponse(
        String id,
        String title,
        String description,
        Instant votingDeadline,
        String organizer,
        LocationData location,
        String status,
        List<ParticipantData> participants,
        List<OptionGroup> optionGroups
) {

    static PollResponse from(PollData pollData, String organizer) {
        var optionGroups = toOptionGroups(pollData.options());
        return new PollResponse(
                pollData.id(),
                pollData.title(),
                pollData.description(),
                pollData.votingDeadline(),
                organizer,
                pollData.location(),
                pollData.status(),
                pollData.participants(),
                optionGroups
        );
    }

    static List<OptionGroup> toOptionGroups(List<OptionData> options) {
        return options.stream()
                .sorted(comparing(OptionData::startAt))
                .collect(groupingBy(option -> toLocalDate(option.startAt())))
                .values()
                .stream()
                .map(option -> {
                    var date = option.getFirst().startAt();
                    return new OptionGroup(
                            toLocalDate(date),
                            option,
                            isWholeDay(option));
                })
                .toList();
    }

    static private LocalDate toLocalDate(Instant instant) {
        return instant.atZone(ZoneId.of("UTC")).toLocalDate();
    }

    static private boolean isWholeDay(List<OptionData> options) {
        return options.stream().anyMatch(OptionData::wholeDay);
    }

    record OptionGroup(
            LocalDate date,
            List<OptionData> options,
            boolean wholeData
    ) {
    }
}
