package com.nodlify.observability.problem.infrastructure

import com.nodlify.observability.problem.application.ProblemUseCaseTestConfig
import org.springframework.data.domain.Pageable
import spock.lang.Execution
import spock.lang.Specification

import static com.nodlify.observability.problem.application.ProblemUseCaseTestConfig.ID
import static com.nodlify.observability.problem.application.ProblemUseCaseTestConfig.setupTestData
import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK


@Execution(SAME_THREAD)
class ProblemLogApiIntegrationSpec extends Specification {

    def problemUseCase = ProblemUseCaseTestConfig.problemLogUseCase()
    def api = new ProblemLogApi(problemUseCase)

    def setup() {
        setupTestData()
    }

    def "should filter problems"() {
        given:
        def pageable = Pageable.ofSize(2)
        def title = "Internal server error"
        def instance = "http://localhost:8080/test/2"
        def detail = "ups... app stopped working... again"
        def status = 500

        when:
        def problems = api.getProblems(pageable, title, status, detail, instance, null, null).content

        then:
        problems.size() == 1
        def actual = problems.first
        actual.title == title
        actual.instance == instance
        actual.detail == detail
        actual.status == status

        and:
        actual.traceId
    }

    def "should get all problems"() {
        given:
        def pageable = Pageable.ofSize(2)

        when:
        def problems = api.getProblems(pageable, null, null, null, null, null, null).content

        then:
        problems.size() == 2
    }

    def "should get problem by id"() {
        when:
        def responseEntity = api.getProblemById(ID.value)

        then:
        responseEntity.statusCode == OK

        and:
        def problemResponse = responseEntity.body
        problemResponse.title == "Internal server error"
        problemResponse.status == 500
    }

    def "should delete problem by id"() {
        when:
        def responseEntity = api.deleteProblem(ID.value)

        then:
        responseEntity.statusCode == NO_CONTENT
    }

    def "should delete all problems"() {
        when:
        def responseEntity = api.deleteAllProblems()

        then:
        responseEntity.statusCode == NO_CONTENT
    }
}
