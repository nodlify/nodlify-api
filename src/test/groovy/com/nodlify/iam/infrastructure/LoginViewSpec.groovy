package com.nodlify.iam.infrastructure

import com.nodlify.iam.application.UserUseCase
import com.nodlify.iam.domain.User
import spock.lang.Specification

class LoginViewSpec extends Specification {

    def userUseCase = Mock(UserUseCase)
    def view = new LoginView(userUseCase)

    def "should show register page"() {
        expect:
        view.registerPage() == "register"
    }

    def "should register user and redirect to login"() {
        when:
        def result = view.register("new@email.com", "Password1", "Sofia Reyes")

        then:
        1 * userUseCase.createUser(_ as User) >> { User user ->
            assert user.email.value == "new@email.com"
            assert user.displayName.value == "Sofia Reyes"
            return user
        }

        and:
        result == "redirect:/login"
    }
}
