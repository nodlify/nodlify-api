package com.pickdate.poll.application;

import com.pickdate.poll.domain.Option;
import com.pickdate.poll.domain.Participant;
import com.pickdate.poll.domain.Poll;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.pickdate.shared.domain.Value.valueOrNull;
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
                poll.isRequireParticipantNames(),
                toParticipants(poll.getParticipants()),
                toOptionData(poll.getOptions()),
                poll.getCreatedAt(),
                poll.getCreatedBy(),
                LocationData.from(poll.getLocation())
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
                .sorted(comparing(OptionData::startAt))
                .toList();
    }
}
