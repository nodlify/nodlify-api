package com.nodlify.poll.application;

import com.nodlify.poll.domain.*;
import com.nodlify.shared.domain.Identifier;

import java.util.List;
import java.util.Optional;


public interface PollUseCase {

    PollData createPoll(CreatePollCommand command);

    PollData updateDetails(Identifier pollId, Title title, Description description);

    PollData addOption(Identifier pollId, Option option);

    PollData getPoll(Identifier id);

    List<PollData> getMyPolls(String owner);

    void deletePoll(Identifier pollId);

    ParticipantData registerParticipant(Identifier pollId, Participant participant);

    Optional<ParticipantData> findParticipant(Identifier pollId, Identifier userId);

    PollData addLocation(Identifier pollId, LocationDetails location);

    PollData removeOption(Identifier pollId, Identifier optionId);

    PollData changeStatus(Identifier pollId, PollStatus status);
}
