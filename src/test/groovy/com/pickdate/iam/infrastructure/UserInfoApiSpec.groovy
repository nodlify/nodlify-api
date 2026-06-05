package com.pickdate.iam.infrastructure

import com.pickdate.iam.application.UserUseCase
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import spock.lang.Specification

import static com.pickdate.test.fixture.AuthenticationFixture.authenticatedUser
import static com.pickdate.test.fixture.UserFixture.*

class UserInfoApiSpec extends Specification {

    def userUseCase = Mock(UserUseCase)
    def userProfileApi = new UserInfoApi(userUseCase)

    def "should return no content when user is not authenticated"() {
        when:
        def response = userProfileApi.getUserInfo(null)

        then:
        response.statusCode.value() == 204
    }

    def "should return no content for anonymous user"() {
        given:
        def authentication = new AnonymousAuthenticationToken(
                "key",
                "anonymousUser",
                [new SimpleGrantedAuthority("ROLE_ANONYMOUS")]
        )

        when:
        def response = userProfileApi.getUserInfo(authentication)

        then:
        response.statusCode.value() == 204
    }

    def "should return user info for authenticated user"() {
        given:
        def user = john()
        def authentication = authenticatedUser()

        when:
        def response = userProfileApi.getUserInfo(authentication)

        then:
        1 * userUseCase.getUserByEmail(JOHN_EMAIL) >> user

        and:
        response.statusCode.value() == 200
        response.body.id() == user.id.value
        response.body.email() == JOHN_EMAIL
        response.body.displayName() == JOHN_DISPLAY_NAME
        response.body.roles() == ["USER"]
    }
}
