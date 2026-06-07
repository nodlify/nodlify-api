package com.nodlify.poll.application;

import java.time.Instant;
import java.util.List;


public record PollData(
        String id,
        String title,
        String description,
        Instant votingDeadline,
        List<ParticipantData> participants,
        List<OptionData> options,
        Instant createdAt,
        String organizer,
        LocationData location,
        String status,
        boolean allowAnonymous,
        String type,
        String choiceType
) {
}
