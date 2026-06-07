package com.nodlify.poll.infrastructure

import com.nodlify.poll.domain.Availability
import com.nodlify.poll.domain.Vote
import com.nodlify.poll.domain.VoteRepository
import com.nodlify.test.stub.VoteDataInitTrait
import com.nodlify.test.type.DataJpaSpec
import org.spockframework.runtime.model.parallel.ExecutionMode
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Execution
import spock.lang.Subject

import static com.nodlify.poll.domain.Vote.VoteId
import static com.nodlify.test.fixture.VoteFixture.*

@Execution(ExecutionMode.SAME_THREAD)
class VoteRepositoryJpaSpec extends DataJpaSpec implements VoteDataInitTrait {

    @Subject
    @Autowired
    VoteRepository voteRepository

    def setup() {
        initData()
    }

    def cleanup() {
        clear()
    }

    def "should save new vote"() {
        given:
        println("id: ")
        println(alice.id)
        def vote = new Vote()
                .with(new VoteId(PARTICIPANT_IDENTIFIER, OPTION_IDENTIFIER))
                .with(POLL_ID)
                .with(Availability.YES)

        when:
        voteRepository.save(vote)

        then:
        voteRepository.findById(vote.id).isPresent()

        and:
        voteRepository.findByPollId(POLL_ID).size() >= 1
    }
}
