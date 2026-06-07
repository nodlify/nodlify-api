package com.nodlify.poll.application

import com.nodlify.poll.domain.Availability
import com.nodlify.poll.domain.Description
import com.nodlify.poll.domain.Poll
import com.nodlify.poll.domain.Title
import com.nodlify.shared.domain.Identifier
import com.nodlify.test.stub.PollRepositoryFake
import com.nodlify.test.stub.VoteRepositoryFake
import spock.lang.Specification

import static com.nodlify.poll.domain.ChoiceType.MULTIPLE
import static com.nodlify.poll.domain.ChoiceType.SINGLE
import static com.nodlify.poll.domain.PollType.SIMPLE

class VoteServiceChoiceSpec extends Specification {

    VoteRepositoryFake votes = new VoteRepositoryFake()
    PollRepositoryFake polls = new PollRepositoryFake()
    VoteService service = new VoteService(votes, polls)

    Identifier participant = Identifier.of("a1b2c3d4-0000-1111-2222-333344445555")
    Identifier optA = Identifier.of("d4e5f6a7-2222-3333-4444-555566667777")
    Identifier optB = Identifier.of("e5f6a7b8-3333-4444-5555-666677778888")

    Poll simplePoll(choiceType) {
        Poll.from(Title.of("Pick a meal"), Description.EMPTY, null, false, SIMPLE, choiceType)
    }

    def "single-choice poll keeps only the latest vote per participant"() {
        given:
        def poll = simplePoll(SINGLE)
        polls.save(poll)

        when:
        service.castVote(new CastVoteCommand(poll.id, participant, optA, Availability.YES))
        service.castVote(new CastVoteCommand(poll.id, participant, optB, Availability.YES))

        then:
        def remaining = votes.findByPollId(poll.id).findAll { it.participantId == participant }
        remaining.size() == 1
        remaining.first().optionId == optB
    }

    def "multiple-choice poll keeps every selected option"() {
        given:
        def poll = simplePoll(MULTIPLE)
        polls.save(poll)

        when:
        service.castVote(new CastVoteCommand(poll.id, participant, optA, Availability.YES))
        service.castVote(new CastVoteCommand(poll.id, participant, optB, Availability.YES))

        then:
        votes.findByPollId(poll.id).findAll { it.participantId == participant }.size() == 2
    }

    def "removeVote deletes a single selection"() {
        given:
        def poll = simplePoll(MULTIPLE)
        polls.save(poll)
        service.castVote(new CastVoteCommand(poll.id, participant, optA, Availability.YES))

        when:
        service.removeVote(poll.id, participant, optA)

        then:
        votes.findByPollId(poll.id).isEmpty()
    }
}
