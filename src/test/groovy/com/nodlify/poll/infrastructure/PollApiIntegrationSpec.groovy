package com.nodlify.poll.infrastructure

import com.nodlify.poll.application.PollUseCaseTestConfig
import com.nodlify.poll.domain.Description
import com.nodlify.poll.domain.Poll
import com.nodlify.poll.domain.Title
import com.nodlify.shared.domain.Identifier
import org.springframework.security.core.Authentication
import spock.lang.Execution
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

import static com.nodlify.poll.application.PollUseCaseTestConfig.pollId
import static com.nodlify.poll.application.PollUseCaseTestConfig.setupTestData
import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD

@Execution(SAME_THREAD)
class PollApiIntegrationSpec extends Specification implements PollApiTrait {

    def pollUseCase = PollUseCaseTestConfig.pollUseCase()

    @Subject
    def controller = new PollApi(pollUseCase)

    def "should get poll"() {
        given:
        setupTestData()

        when:
        def response = controller.getPoll(pollId)

        then:
        response.statusCode.value() == 200

        and:
        response.body.id() == pollId
    }

    def "should create poll"() {
        given:
        def req = createPollRequest()

        when:
        def response = controller.createPoll(req)

        then:
        def poll = response.body

        poll.title() == title()
        poll.description() == description()
        poll.votingDeadline() == votingDeadline()
        poll.requireParticipantNames() == requireParticipantNames()
    }

    def "should delete poll"() {
        given:
        setupTestData()

        when:
        def response = controller.deletePoll(pollId)

        then:
        response.statusCode.value() == 204
    }

    def "should return 204 when trying to delete not existing poll"() {
        given:
        setupTestData()

        when:
        def response = controller.deletePoll("id doesn't exist")

        then:
        response.statusCode.value() == 204
    }

    def "should add option"() {
        given:
        setupTestData()
        def request = createOptionRequest()

        when:
        def response = controller.addOption(pollId, request)

        then:
        response.statusCode.value() == 201

        response.body.options().find { option ->
            option.startAt() == Instant.parse("2995-08-01T10:00:00Z") &&
                    option.endAt() == Instant.parse("2995-08-01T11:00:00Z")
        }
    }

    def "should register participant"() {
        given:
        setupTestData()

        def req = registerParticipantRequest()

        when:
        controller.registerParticipant(pollId, req)

        then:
        def poll = pollUseCase.getPoll(Identifier.of(pollId))

        poll.participants().find {
            it.name() == req.toDisplayName().value
        }
    }

    def "should remove option"() {
        given:
        setupTestData()
        def optionId = pollUseCase.getPoll(Identifier.of(pollId)).options().first().optionId()

        when:
        def response = controller.removeOption(pollId, optionId)

        then:
        response.statusCode.value() == 200
        !response.body.options().any { it.optionId() == optionId }
    }

    def "should return polls for authenticated user"() {
        given:
        def owner = "owner@example.com"
        def poll = Poll.from(Title.of("Owner's poll"), Description.of("desc"))
        poll.@createdBy = owner
        PollUseCaseTestConfig.repository.save(poll)

        def auth = Mock(Authentication) { getName() >> owner }

        when:
        def response = controller.getMyPolls(auth)

        then:
        response.statusCode.value() == 200
        response.body.any { it.title() == "Owner's poll" }
    }
}
