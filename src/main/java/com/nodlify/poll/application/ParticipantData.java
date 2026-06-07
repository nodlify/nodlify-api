package com.nodlify.poll.application;

import com.nodlify.poll.domain.Participant;

import static com.nodlify.shared.domain.Value.valueOrNull;


public record ParticipantData(
        String id,
        String name,
        String email,
        String phone
) {

    public static ParticipantData from(Participant participant) {
        return new ParticipantData(
                valueOrNull(participant.getId()),
                valueOrNull(participant.getDisplayName()),
                valueOrNull(participant.getEmail()),
                valueOrNull(participant.getPhone())
        );
    }
}
