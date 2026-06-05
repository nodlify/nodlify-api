package com.nodlify.iam.infrastructure

import com.nodlify.iam.application.UserUseCase
import com.nodlify.iam.domain.User
import com.nodlify.iam.domain.UserData
import com.nodlify.iam.domain.UserNotFoundException
import com.nodlify.shared.domain.Identifier
import com.nodlify.test.mapper.JsonMapper
import com.nodlify.test.type.MvcSpec
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Requires

import static com.nodlify.test.fixture.UserFixture.SOME_ADMIN
import static com.nodlify.test.fixture.UserFixture.SOME_USER
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@Requires({ it.env['INCLUDE_SLOW_TESTS'] == 'true' })
class UserApiMvcSpec extends MvcSpec implements JsonMapper {

    @Autowired
    MockMvc mvc

    @SpringBean
    UserUseCase userUseCase = Mock()

    def "should return list of users"() {
        given:
        def user = SOME_USER
        def admin = SOME_ADMIN
        def page = new PageImpl<User>([admin, user], PageRequest.of(0, 20), 2)

        when:
        def response = mvc.perform(get("/api/v1/iam/users")
                .param("page", "0")
                .param("size", "20"))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        1 * userUseCase.getAllUsers(_ as Pageable) >> page

        and:
        response.status == 200

        and:
        def map = toMap(response)
        map.content.any { it.email == admin.email.value }
        map.content.any { it.email == user.email.value }
    }

    def "should return user by id"() {
        given:
        def user = SOME_USER

        when:
        def response = mvc.perform(get("/api/v1/iam/users/${user.id.value}"))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        1 * userUseCase.getUserById(user.id.value) >> user

        and:
        response.status == 200

        and:
        def userData = toObject(response.contentAsString, UserData)
        userData.id() == user.id.value
        userData.email() == user.email.value
        userData.roles().contains("USER")
    }

    def "should create user"() {
        given:
        def request = new CreateUserRequest("email@email.com", "Password1", "Admin")

        when:
        def response = mvc.perform(post("/api/v1/iam/users")
                .content(toJson(request)).contentType(APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        1 * userUseCase.createUser(_ as User) >> { User user -> return user }

        and:
        response.status == 201

        and:
        def body = toObject(response.contentAsString, UserData)
        body.email() == "email@email.com"
        !body.id().isBlank()
    }

    def "should return 400, when value is invalid"() {
        given:
        def request = new CreateUserRequest("incorrect@", "Password1", "Admin")

        when:
        def response = mvc.perform(post("/api/v1/iam/users")
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
        body.instance.contains("/api/v1/iam/users")
        !body.traceId.isBlank()

        and:
        with(body.invalidParams[0] as Map<String, String>) {
            name == "email"
            value == "incorrect@"
            reason == "Invalid email format"
        }
    }

    def "should return 404, when user not found"() {
        given:
        def id = Identifier.generate()

        when:
        def response = mvc.perform(get("/api/v1/iam/users/${id.value}"))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        1 * userUseCase.getUserById(id.value) >> { throw UserNotFoundException.withId(id.value) }

        and:
        response.status == 404

        and:
        def body = toMap(response)
        body.title == "Resource Not Found"
        body.status == 404
        body.detail == "User not found"
        body.instance.contains("/api/v1/iam/users")
        !body.traceId.isBlank()

        and:
        with(body.invalidParams[0] as Map<String, String>) {
            name == "id"
            value == id.value
            reason == "User not found"
        }
    }

    def "should delete user"() {
        given:
        def id = Identifier.generate()

        when:
        def response = mvc.perform(delete("/api/v1/iam/users/${id.value}"))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        1 * userUseCase.deleteUser(id.value)

        and:
        response.status == 204
    }

    def "should return 404, when deleting non-existent user"() {
        given:
        def id = Identifier.generate()

        when:
        def response = mvc.perform(delete("/api/v1/iam/users/${id.value}"))
                .andDo(print())
                .andReturn()
                .getResponse()

        then:
        1 * userUseCase.deleteUser(id.value) >> { throw UserNotFoundException.withId(id.value) }

        and:
        response.status == 404

        and:
        def body = toMap(response)
        body.title == "Resource Not Found"
        body.status == 404
        body.detail == "User not found"
        body.instance.contains("/api/v1/iam/users")
        !body.traceId.isBlank()

        and:
        with(body.invalidParams[0] as Map<String, String>) {
            name == "id"
            value == id.value
            reason == "User not found"
        }
    }
}
