package com.nodlify.poll.application;

import com.nodlify.shared.domain.Identifier;

import java.util.List;


public interface VoteUseCase {

    void castVote(CastVoteCommand castVoteCommand);

    List<VoteData> getVotesBy(Identifier pollId);
}
