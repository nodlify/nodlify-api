package com.nodlify.poll.infrastructure

import com.nodlify.poll.application.VoteUseCase
import com.nodlify.test.type.MvcSpec
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Execution

import static com.nodlify.test.fixture.VoteFixture.castVoteRequestJson
import static com.nodlify.test.fixture.VoteFixture.someVoteData
import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@Execution(SAME_THREAD)
class VoteApiMvcSpec extends MvcSpec {

    @Autowired
    MockMvc mvc

    @SpringBean
    VoteUseCase voteUseCase = Mock()

    @SpringBean
    PollEventPublisher eventPublisher = Mock()

    @WithMockUser(authorities = "ROLE_USER")
    def "should be able to get votes"() {
        given:
        def pollId = "123"
        def votes = someVoteData()

        when:
        def response = mvc.perform(get("/api/v1/polls/${pollId}/votes"))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        1 * voteUseCase.getVotesBy(_) >> votes

        and:
        response.status == 200
    }

    @WithMockUser(authorities = "ROLE_USER")
    def "should be able to cast vote"() {
        given:
        def pollId = "123"
        def req = castVoteRequestJson()

        when:
        def response = mvc.perform(
                post("/api/v1/polls/${pollId}/votes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        1 * voteUseCase.castVote(_)

        and:
        response.status == 201
    }
}
