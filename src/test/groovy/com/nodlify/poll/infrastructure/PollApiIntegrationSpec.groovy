package com.nodlify.poll.infrastructure

import com.nodlify.iam.application.UserData
import com.nodlify.iam.application.UserUseCase
import com.nodlify.iam.domain.UserNotFoundException
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
    def userUseCase = Mock(UserUseCase)
    def controller = new PollApi(pollUseCase, userUseCase)

    def "should get poll"() {
        given:
        setupTestData()
        def organizerEmail = "owner@example.com"
        def poll = PollUseCaseTestConfig.repository.findById(Identifier.of(pollId)).get()
        poll.@createdBy = organizerEmail
        PollUseCaseTestConfig.repository.save(poll)
        userUseCase.getUserByEmail(organizerEmail) >> new UserData(
                "owner-id",
                organizerEmail,
                "Poll Owner",
                ["USER"]
        )

        when:
        def response = controller.getPoll(pollId)

        then:
        response.statusCode.value() == 200

        and:
        response.body.id() == pollId
        response.body.organizer() == "Poll Owner"
    }

    def "should use organizer email when user profile is missing"() {
        given:
        setupTestData()
        def organizerEmail = "missing-owner@example.com"
        def poll = PollUseCaseTestConfig.repository.findById(Identifier.of(pollId)).get()
        poll.@createdBy = organizerEmail
        PollUseCaseTestConfig.repository.save(poll)
        userUseCase.getUserByEmail(organizerEmail) >> {
            throw UserNotFoundException.withEmail(organizerEmail)
        }

        when:
        def response = controller.getPoll(pollId)

        then:
        response.body.organizer() == organizerEmail
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
        controller.registerParticipant(pollId, req, null)

        then:
        def poll = pollUseCase.getPoll(Identifier.of(pollId))

        poll.participants().find {
            it.name() == req.toDisplayName().value
        }
    }

    def "should register authenticated user from profile without request body"() {
        given:
        setupTestData()
        def email = "signed-in@example.com"
        def auth = Mock(Authentication) {
            isAuthenticated() >> true
            getName() >> email
        }
        userUseCase.getUserByEmail(email) >> new UserData(
                "user-id",
                email,
                "Signed In User",
                ["USER"]
        )

        when:
        def first = controller.registerParticipant(pollId, null, auth)
        def second = controller.registerParticipant(pollId, null, auth)

        then:
        first.body.id() == second.body.id()
        first.body.name() == "Signed In User"
        first.body.email() == email

        and:
        pollUseCase.getPoll(Identifier.of(pollId)).participants()
                .count { it.email() == email } == 1
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
