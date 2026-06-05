package com.pickdate.poll.application;

import com.pickdate.poll.domain.*;
import com.pickdate.shared.domain.Identifier;
import com.pickdate.shared.domain.Property;
import com.pickdate.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@CacheConfig(cacheNames = "polls")
public class PollService implements PollUseCase {

    private final PollRepository repository;

    @Override
    @CacheEvict(allEntries = true)
    public PollData createPoll(Title title, Description description) {
        return createPoll(title, description, null, List.of());
    }

    @Override
    @CacheEvict(allEntries = true)
    public PollData createPoll(
            Title title,
            Description description,
            LocationDetails location,
            List<Option> options
    ) {
        return createPoll(title, description, location, options, null, true);
    }

    @Override
    @CacheEvict(allEntries = true)
    public PollData createPoll(
            Title title,
            Description description,
            LocationDetails location,
            List<Option> options,
            Instant votingDeadline,
            boolean requireParticipantNames
    ) {
        var poll = Poll.from(title, description, votingDeadline, requireParticipantNames);
        if (location != null) {
            poll.addLocation(GeoLocation.of(location));
        }
        poll.addOptions(options);

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
    public PollData registerParticipant(Identifier pollId, Participant participant) {
        var poll = findPoll(pollId);
        poll.addParticipant(participant);
        repository.save(poll);
        return PollMapper.toPollData(poll);
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
