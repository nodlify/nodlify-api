package com.nodlify.poll.application;

import com.nodlify.poll.domain.ChoiceType;
import com.nodlify.poll.domain.PollRepository;
import com.nodlify.poll.domain.Vote;
import com.nodlify.poll.domain.VoteRepository;
import com.nodlify.shared.domain.Identifier;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@CacheConfig(cacheNames = "votes")
class VoteService implements VoteUseCase {

    private final VoteRepository repository;
    private final PollRepository pollRepository;

    @Override
    @CacheEvict(key = "#command.pollId().getValue()")
    public void castVote(CastVoteCommand command) {
        if (isSingleChoice(command.pollId())) {
            clearVotes(command.pollId(), command.participantId(), command.OptionId());
        }
        var vote = new Vote()
                .with(new Vote.VoteId(command.participantId(), command.OptionId()))
                .with(command.pollId())
                .with(command.vote());
        repository.save(vote);
    }

    @Override
    @CacheEvict(key = "#pollId.getValue()")
    public void removeVote(Identifier pollId, Identifier participantId, Identifier optionId) {
        repository.deleteById(new Vote.VoteId(participantId, optionId));
    }

    private boolean isSingleChoice(Identifier pollId) {
        return pollRepository.findById(pollId)
                .map(poll -> poll.getChoiceType() == ChoiceType.SINGLE)
                .orElse(false);
    }

    private void clearVotes(Identifier pollId, Identifier participantId, Identifier keep) {
        repository.findByPollId(pollId).stream()
                .filter(vote -> participantId.equals(vote.getParticipantId()))
                .filter(vote -> !keep.equals(vote.getOptionId()))
                .forEach(vote -> repository.deleteById(vote.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(key = "#pollId.getValue()")
    public List<VoteData> getVotesBy(Identifier pollId) {
        return repository.findByPollId(pollId).stream()
                .map(VoteData::from)
                .toList();
    }
}
