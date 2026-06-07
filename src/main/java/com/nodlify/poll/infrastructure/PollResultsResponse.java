package com.nodlify.poll.infrastructure;

import com.nodlify.poll.application.OptionData;
import com.nodlify.poll.application.PollData;
import com.nodlify.poll.application.VoteData;

import java.util.List;


record PollResultsResponse(
        String status,
        String type,
        String choiceType,
        List<OptionData> options,
        List<Participant> participants,
        List<VoteData> votes
) {

    static PollResultsResponse from(PollData poll, List<VoteData> votes) {
        var participants = poll.participants().stream()
                .map(participant -> new Participant(participant.id(), participant.name()))
                .toList();
        return new PollResultsResponse(
                poll.status(),
                poll.type(),
                poll.choiceType(),
                poll.options(),
                participants,
                votes);
    }

    record Participant(String id, String name) {
    }
}
