package com.nodlify.poll.infrastructure;

import com.nodlify.poll.application.*;

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
        boolean allowAnonymous,
        String type,
        String choiceType,
        List<ParticipantData> participants,
        List<OptionData> options,
        List<OptionGroup> optionGroups
) {

    static PollResponse from(PollData pollData, String organizer) {
        return new PollResponse(
                pollData.id(),
                pollData.title(),
                pollData.description(),
                pollData.votingDeadline(),
                organizer,
                pollData.location(),
                pollData.status(),
                pollData.allowAnonymous(),
                pollData.type(),
                pollData.choiceType(),
                pollData.participants(),
                pollData.options(),
                toOptionGroups(pollData.options())
        );
    }

    static List<OptionGroup> toOptionGroups(List<OptionData> options) {
        return options.stream()
                .filter(TimeOptionData.class::isInstance)
                .map(TimeOptionData.class::cast)
                .sorted(comparing(TimeOptionData::startAt))
                .collect(groupingBy(option -> toLocalDate(option.startAt())))
                .values()
                .stream()
                .map(group -> new OptionGroup(
                        toLocalDate(group.getFirst().startAt()),
                        group,
                        isWholeDay(group)))
                .sorted(comparing(OptionGroup::date))
                .toList();
    }

    static private LocalDate toLocalDate(Instant instant) {
        return instant.atZone(ZoneId.of("UTC")).toLocalDate();
    }

    static private boolean isWholeDay(List<TimeOptionData> options) {
        return options.stream().anyMatch(TimeOptionData::wholeDay);
    }

    record OptionGroup(
            LocalDate date,
            List<TimeOptionData> options,
            boolean wholeData
    ) {
    }
}
