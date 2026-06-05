package com.nodlify.observability.audit.domain

import com.nodlify.shared.exception.IllegalValueException
import spock.lang.Specification


class ActionSpec extends Specification {

    def "should create valid action with underscores"() {
        expect:
        Action.of("login_failed_event").value == "login_failed_event"
    }

    def "should reject action with consecutive underscores"() {
        when:
        Action.of("login__failed")

        then:
        thrown(IllegalValueException)
    }

    def "should reject long invalid action without stack overflow"() {
        given:
        def action = "a_" * 20_000

        when:
        Action.of(action)

        then:
        thrown(IllegalValueException)
    }
}

