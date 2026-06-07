package com.nodlify.poll.application;

import com.nodlify.shared.domain.Identifier;

import java.util.List;


public interface VoteUseCase {

    void castVote(CastVoteCommand castVoteCommand);

    void removeVote(Identifier pollId, Identifier participantId, Identifier optionId);

    List<VoteData> getVotesBy(Identifier pollId);
}
