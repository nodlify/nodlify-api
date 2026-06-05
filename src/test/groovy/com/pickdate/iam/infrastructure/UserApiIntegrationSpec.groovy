package com.pickdate.iam.infrastructure

import com.pickdate.iam.application.UserUseCaseTestConfig
import com.pickdate.shared.exception.IllegalValueException
import org.springframework.data.domain.Pageable
import spock.lang.Execution
import spock.lang.Specification

import static com.pickdate.test.fixture.UserFixture.SOME_ADMIN
import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD


@Execution(SAME_THREAD)
class UserApiIntegrationSpec extends Specification {

    def userUseCase = UserUseCaseTestConfig.userUseCase()
    def userApi = new UserApi(userUseCase)

    def setup() {
        UserUseCaseTestConfig.setupTestData()
    }

    def "should list all users"() {
        given:
        def pageable = Pageable.ofSize(2)

        when:
        def responseEntity = userApi.getAllUsers(pageable)

        then:
        responseEntity.body.content.find { it.email() == "admin@email.com" }
        responseEntity.body.content.find { it.email() == "user@email.com" }
    }

    def "should get user by id"() {
        given:
        def id = SOME_ADMIN.id.value

        when:
        def responseEntity = userApi.getById(id)

        then:
        responseEntity.body.email() == "admin@email.com"
        responseEntity.body.roles() == ["ADMIN"]
    }

    def "should create a new user account"() {
        given:
        def email = "newemail@email.com"
        def pass = "superSecretPassword!"
        def display = "Admin"
        def request = new CreateUserRequest(email, pass, display)

        when:
        def responseEntity = userApi.create(request)

        then:
        responseEntity.body.email() == email
        responseEntity.body.roles() == ["USER"]
        // id is not null
        responseEntity.body.id()
    }

    def "should reject user account without display name"() {
        given:
        def request = new CreateUserRequest("newemail@email.com", "superSecretPassword!", null)

        when:
        userApi.create(request)

        then:
        def ex = thrown(IllegalValueException)
        ex.detail == "displayName must not be blank"
    }

    def "should delete user"() {
        given:
        def id = SOME_ADMIN.id.value

        when:
        def responseEntity = userApi.delete(id)

        then:
        responseEntity.statusCode.value() == 204
        responseEntity.body == null
    }
}
