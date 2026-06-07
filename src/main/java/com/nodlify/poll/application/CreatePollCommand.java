package com.nodlify.poll.application;

import com.nodlify.poll.domain.*;

import java.time.Instant;
import java.util.List;


public record CreatePollCommand(
        Title title,
        Description description,
        LocationDetails location,
        List<Option> options,
        Instant votingDeadline,
        boolean allowAnonymous,
        PollType type,
        ChoiceType choiceType
) {
}
