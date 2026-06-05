package com.nodlify.poll.domain;

import com.nodlify.shared.domain.Identifier;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


public interface PollRepository extends Serializable {

    Poll save(Poll poll);

    Optional<Poll> findById(Identifier id);

    List<Poll> findByCreatedBy(String createdBy);

    void deleteById(Identifier pollId);

    void deleteAll();
}
