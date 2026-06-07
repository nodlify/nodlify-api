package com.nodlify.poll.domain;

import com.nodlify.shared.domain.Identifier;

import java.util.List;
import java.util.Optional;


public interface VoteRepository {

    List<Vote> findByPollId(Identifier pollId);

    Vote save(Vote vote);

    Optional<Vote> findById(Vote.VoteId voteId);

    void deleteById(Vote.VoteId voteId);
}
