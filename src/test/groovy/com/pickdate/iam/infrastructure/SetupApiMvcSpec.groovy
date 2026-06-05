package com.pickdate.iam.infrastructure

import com.pickdate.iam.application.ApplicationSetupUseCase
import com.pickdate.shared.exception.InternalServerError
import com.pickdate.test.mapper.JsonMapper
import com.pickdate.test.type.MvcSpec
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Requires

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@Requires({ it.env['INCLUDE_SLOW_TESTS'] == 'true' })
class SetupApiMvcSpec extends MvcSpec implements JsonMapper {

    @Autowired
    MockMvc mvc

    @SpringBean
    ApplicationSetupUseCase applicationSetupUseCase = Mock()

    def "should set domain for the application"() {
        given:
        def request = new SetupDomainRequest("http://localhost:8081")

        when:
        def response = mvc.perform(post("/api/v1/iam/setup/domain")
                .content(toJson(request)).contentType(APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        1 * applicationSetupUseCase.setupDomain(request.toDomainUrl())

        and:
        response.status == 201
    }

    def "should create initial admin user"() {
        given:
        def request = new CreateUserRequest("email@email.com", "Password1", "Admin")

        when:
        def response = mvc.perform(post("/api/v1/iam/setup/admin")
                .content(toJson(request)).contentType(APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        1 * applicationSetupUseCase.setupAdmin(_)

        and:
        response.status == 201
    }

    def "should complete setup"() {
        when:
        def response = mvc.perform(post("/api/v1/iam/setup"))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        1 * applicationSetupUseCase.completeSetup()

        and:
        response.status == 200
    }

    def "should return 400, when value is invalid"() {
        given:
        def request = new CreateUserRequest("incorrect@", "superSecretPass!", "Admin")

        when:
        def response = mvc.perform(post("/api/v1/iam/setup/admin")
                .content(toJson(request)).contentType(APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        response.status == 400

        and:
        def body = toMap(response)
        body.title == "Validation Error"
        body.status == 400
        body.detail == "Invalid email format"
        body.instance.contains("/api/v1/iam/setup/admin")
        !body.traceId.isBlank()

        and:
        with(body.invalidParams[0] as Map<String, String>) {
            name == "email"
            value == "incorrect@"
            reason == "Invalid email format"
        }
    }

    def "should return 500, when there was internal server error"() {
        when:
        def response = mvc.perform(post("/api/v1/iam/setup"))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        1 * applicationSetupUseCase.completeSetup() >> { throw new InternalServerError("ups... app stopped working") }

        and:
        response.status == 500

        and:
        def body = toMap(response)
        body.title == "Internal Server Error"
        body.status == 500
        body.detail == "ups... app stopped working"
        body.instance.contains("/api/v1/iam/setup")
        !body.traceId.isBlank()
    }
}
