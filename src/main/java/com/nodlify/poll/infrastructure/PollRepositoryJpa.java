package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.Poll;
import com.nodlify.poll.domain.PollRepository;
import com.nodlify.shared.domain.Identifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
interface PollRepositoryJpa extends PollRepository, JpaRepository<Poll, Identifier> {
}
