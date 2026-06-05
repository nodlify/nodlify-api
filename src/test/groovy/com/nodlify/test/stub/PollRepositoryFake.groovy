package com.nodlify.test.stub

import com.nodlify.poll.domain.Poll
import com.nodlify.poll.domain.PollRepository
import com.nodlify.shared.domain.Identifier

import java.util.concurrent.ConcurrentHashMap

class PollRepositoryFake implements PollRepository {

    Map<Identifier, Poll> map = new ConcurrentHashMap<>()

    @Override
    Poll save(Poll poll) {
        map.put(poll.id, poll)
        return poll
    }

    @Override
    Optional<Poll> findById(Identifier id) {
        Optional.ofNullable(map.get(id))
    }

    @Override
    List<Poll> findByCreatedBy(String createdBy) {
        return map.values().findAll { it.createdBy == createdBy }.toList()
    }

    @Override
    void deleteById(Identifier pollId) {
        map.remove(pollId)
    }

    void deleteAll() {
        map.clear()
    }
}
