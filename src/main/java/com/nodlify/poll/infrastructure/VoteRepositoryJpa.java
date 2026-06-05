package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.Vote;
import com.nodlify.poll.domain.Vote.VoteId;
import com.nodlify.poll.domain.VoteRepository;
import com.nodlify.shared.domain.Identifier;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
interface VoteRepositoryJpa extends VoteRepository, JpaRepository<Vote, VoteId> {

    @NotNull
    @Override
    Optional<Vote> findById(@NotNull VoteId voteId);

    @Override
    List<Vote> findByPollId(Identifier pollId);

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    Vote save(@NotNull Vote vote);
}
