package com.nodlify.poll.application;

import com.nodlify.poll.domain.Option;
import com.nodlify.poll.domain.Participant;
import com.nodlify.poll.domain.Poll;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.nodlify.shared.domain.Value.valueOrNull;
import static java.util.Comparator.comparing;


final class PollMapper {

    private PollMapper() {
    }

    static PollData toPollData(Poll poll) {

        return new PollData(
                valueOrNull(poll.getId()),
                valueOrNull(poll.getTitle()),
                valueOrNull(poll.getDescription()),
                poll.getVotingDeadline(),
                toParticipants(poll.getParticipants()),
                toOptionData(poll.getOptions()),
                poll.getCreatedAt(),
                poll.getCreatedBy(),
                LocationData.from(poll.getLocation()),
                poll.status(Instant.now()).name(),
                poll.isAllowAnonymous(),
                poll.getType().name(),
                poll.getChoiceType().name()
        );
    }

    static List<ParticipantData> toParticipants(Set<Participant> participants) {
        return participants.stream()
                .map(ParticipantData::from)
                .sorted(Comparator.comparing(ParticipantData::name))
                .toList();
    }

    static List<OptionData> toOptionData(Set<Option> options) {
        return options.stream()
                .map(OptionData::from)
                .sorted(comparing(PollMapper::sortKey))
                .toList();
    }

    private static String sortKey(OptionData option) {
        return switch (option) {
            case TimeOptionData time -> "0:" + time.startAt();
            case TextOptionData text -> "1:" + text.label();
        };
    }
}
