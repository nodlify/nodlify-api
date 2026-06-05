package com.pickdate.poll.application;

import com.pickdate.poll.domain.*;
import com.pickdate.shared.domain.Identifier;

import java.time.Instant;
import java.util.List;


public interface PollUseCase {

    PollData createPoll(Title title, Description description);

    PollData createPoll(Title title, Description description, LocationDetails location, List<Option> options);

    PollData createPoll(
            Title title,
            Description description,
            LocationDetails location,
            List<Option> options,
            Instant votingDeadline,
            boolean requireParticipantNames
    );

    PollData updateDetails(Identifier pollId, Title title, Description description);

    PollData addOption(Identifier pollId, Option option);

    PollData getPoll(Identifier id);

    List<PollData> getMyPolls(String owner);

    void deletePoll(Identifier pollId);

    PollData registerParticipant(Identifier pollId, Participant participant);

    PollData addLocation(Identifier pollId, LocationDetails location);

    PollData removeOption(Identifier pollId, Identifier optionId);
}
