package com.nodlify.poll.application;


import com.nodlify.poll.domain.Vote;

import static com.nodlify.shared.domain.Value.valueOrNull;

public record VoteData(
        String pollId,
        String participantId,
        String optionId,
        String availability
) {

    public static VoteData from(Vote vote) {
        return new VoteData(
                valueOrNull(vote.getPollId()),
                valueOrNull(vote.getParticipantId()),
                valueOrNull(vote.getOptionId()),
                vote.getAvailability().name()
        );
    }
}
