package com.nodlify.test.stub

import com.nodlify.poll.domain.Vote
import com.nodlify.poll.domain.VoteRepository
import com.nodlify.shared.domain.Identifier

import java.util.concurrent.ConcurrentHashMap

class VoteRepositoryFake implements VoteRepository {

    Map<Vote.VoteId, Vote> map = new ConcurrentHashMap()

    @Override
    List<Vote> findByPollId(Identifier pollId) {
        map.values().findAll { it.pollId == pollId }
    }

    @Override
    Vote save(Vote vote) {
        map.put(vote.id, vote)
        return vote
    }

    @Override
    Optional<Vote> findById(Vote.VoteId voteId) {
        Optional.ofNullable(map.get(voteId))
    }

    @Override
    void deleteById(Vote.VoteId voteId) {
        map.remove(voteId)
    }
}
