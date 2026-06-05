package com.nodlify.iam.infrastructure

import com.nodlify.iam.domain.UserNotFoundException
import com.nodlify.iam.domain.UserRepository
import com.nodlify.shared.domain.Email
import com.nodlify.test.fixture.UserFixture
import org.springframework.security.core.userdetails.UserDetails
import spock.lang.Specification

class UserAuthenticationServiceSpec extends Specification {

    def userRepository = Mock(UserRepository)
    def userAuthenticationService = new UserAuthenticationService(userRepository)

    def "should load user details when user exists"() {
        given:
        def user = UserFixture.SOME_ADMIN

        and:
        userRepository.findByEmail(Email.of(user.email.value)) >> Optional.of(user)

        when:
        UserDetails result = userAuthenticationService.loadUserByUsername(user.email.value)

        then:
        result.username == user.email.value
        result.password == user.password.value
    }

    def "should throw UserNotFoundException when user does not exist"() {
        given:
        Email email = Email.of("missing@email.com")

        when:
        userAuthenticationService.loadUserByUsername(email.value)

        then:
        1 * userRepository.findByEmail(email) >> Optional.empty()

        def ex = thrown(UserNotFoundException)
        ex.detail == "User not found"
        ex.property.name() == "email"
        ex.property.value() == email.value
    }
}
