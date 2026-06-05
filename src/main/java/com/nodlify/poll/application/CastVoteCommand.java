package com.nodlify.poll.application;

import com.nodlify.poll.domain.Availability;
import com.nodlify.shared.domain.Identifier;


public record CastVoteCommand(
        Identifier pollId,
        Identifier participantId,
        Identifier OptionId,
        Availability vote
) {
}
