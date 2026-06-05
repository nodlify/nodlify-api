package com.nodlify.poll.infrastructure;

import com.nodlify.poll.application.CastVoteCommand;
import com.nodlify.poll.domain.Availability;
import com.nodlify.shared.domain.Identifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
class CastVoteRequest {

    private String participantId;
    private String optionId;
    private String availability;

    CastVoteCommand toCommand(String pollId) {
        return new CastVoteCommand(
                Identifier.of(pollId),
                Identifier.of(participantId),
                Identifier.of(optionId),
                Availability.from(availability)
        );
    }
}
