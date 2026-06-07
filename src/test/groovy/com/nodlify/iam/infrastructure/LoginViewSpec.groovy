package com.nodlify.iam.infrastructure

import com.nodlify.iam.application.CreateUserCommand
import com.nodlify.iam.application.UserUseCase
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
        1 * userUseCase.createUser(_ as CreateUserCommand) >> { CreateUserCommand command ->
            assert command.email() == "new@email.com"
            assert command.displayName() == "Sofia Reyes"
            return null
        }

        and:
        result == "redirect:/login"
    }
}
