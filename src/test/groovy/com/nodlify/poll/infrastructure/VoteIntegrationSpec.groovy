package com.nodlify.poll.infrastructure

import spock.lang.Specification
import spock.lang.Subject

import static com.nodlify.poll.application.VoteUseCaseTestConfig.setupFakeData
import static com.nodlify.poll.application.VoteUseCaseTestConfig.voteUseCase


class VoteIntegrationSpec extends Specification implements VoteApiTrait {

    @Subject
    def controller = new VoteApi(voteUseCase())

    def "should be able to cast vote"() {
        given:
        def pollId = pollId()
        def request = createCastVoteRequest()

        when:
        def response = controller.castVote(pollId, request)

        then:
        response.statusCode.value() == 201
    }

    def "should get votes"() {
        given:
        setupFakeData()
        def pollId = pollId()

        when:
        def response = controller.getVotes(pollId)

        then:
        response.statusCode.value() == 200

        and:
        response.body.size() >= 2
    }
}
