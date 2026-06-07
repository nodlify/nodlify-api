package com.nodlify.poll.application;

import com.nodlify.poll.domain.*;
import com.nodlify.shared.domain.Identifier;
import com.nodlify.shared.domain.Property;
import com.nodlify.shared.domain.Value;
import com.nodlify.shared.exception.IllegalValueException;
import com.nodlify.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
@CacheConfig(cacheNames = "polls")
public class PollService implements PollUseCase {

    private final PollRepository repository;

    @Override
    @CacheEvict(allEntries = true)
    public PollData createPoll(CreatePollCommand command) {
        var poll = Poll.from(
                command.title(),
                command.description(),
                command.votingDeadline(),
                command.allowAnonymous(),
                command.type(),
                command.choiceType());
        if (command.location() != null) {
            poll.addLocation(GeoLocation.of(command.location()));
        }
        poll.addOptions(command.options() == null ? List.of() : command.options());

        var saved = repository.save(poll);
        return PollMapper.toPollData(saved);
    }

    @Override
    @CacheEvict(key = "#pollId.value")
    public void deletePoll(Identifier pollId) {
        repository.deleteById(pollId);
    }

    @Override
    @CacheEvict(key = "#pollId.value")
    public ParticipantData registerParticipant(Identifier pollId, Participant participant) {
        var poll = findPoll(pollId);
        if (!poll.isAllowAnonymous() && Value.valueOrNull(participant.getDisplayName()) == null) {
            throw new IllegalValueException(Property.of("displayName", null), "Participant name is required");
        }
        var registered = poll.addParticipant(participant);
        repository.save(poll);
        return ParticipantData.from(registered);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParticipantData> findParticipant(Identifier pollId, Identifier userId) {
        return findPoll(pollId).findParticipant(userId).map(ParticipantData::from);
    }

    @Override
    @CacheEvict(key = "#pollId.value")
    public PollData updateDetails(Identifier pollId, Title title, Description description) {
        var poll = findPoll(pollId);
        poll.updateDetails(title, description);
        repository.save(poll);
        return PollMapper.toPollData(poll);
    }

    @Override
    @CacheEvict(key = "#pollId.value")
    public PollData addOption(Identifier pollId, Option option) {
        var poll = findPoll(pollId);
        poll.addOption(option);
        repository.save(poll);
        return PollMapper.toPollData(poll);
    }

    @Override
    @CacheEvict(key = "#pollId.value")
    public PollData removeOption(Identifier pollId, Identifier optionId) {
        var poll = findPoll(pollId);
        poll.removeOption(optionId);
        repository.save(poll);
        return PollMapper.toPollData(poll);
    }

    @Override
    @CacheEvict(key = "#pollId.value")
    public PollData changeStatus(Identifier pollId, PollStatus status) {
        var poll = findPoll(pollId);
        poll.changeStatus(status);
        repository.save(poll);
        return PollMapper.toPollData(poll);
    }

    @Override
    @Cacheable(key = "#id.value")
    @Transactional(readOnly = true)
    public PollData getPoll(Identifier id) {
        var poll = findPoll(id);
        return PollMapper.toPollData(poll);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PollData> getMyPolls(String owner) {
        return repository.findByCreatedBy(owner)
                .stream()
                .map(PollMapper::toPollData)
                .toList();
    }

    @Override
    @CacheEvict(key = "#pollId.value")
    public PollData addLocation(Identifier pollId, LocationDetails location) {
        var poll = findPoll(pollId);
        var geoLocation = GeoLocation.of(location);
        poll.addLocation(geoLocation);
        repository.save(poll);
        return PollMapper.toPollData(poll);
    }

    private Poll findPoll(Identifier id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(new Property<>("id", id), "Poll not found"));
    }
}
