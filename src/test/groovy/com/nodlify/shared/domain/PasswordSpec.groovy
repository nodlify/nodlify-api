package com.nodlify.shared.domain

import com.nodlify.iam.domain.Password
import com.nodlify.iam.domain.PlaintextPassword
import com.nodlify.shared.exception.IllegalValueException
import spock.lang.Specification

class PasswordSpec extends Specification {

    def "should throw exception when password is null"() {
        when:
        Password.fromPlaintext(null)

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when password is blank"() {
        when:
        Password.fromPlaintext("")

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when password is too short"() {
        given:
        def password = "Aa1aa"

        when:
        Password.fromPlaintext(password)

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when password is too long"() {
        given:
        def longBase = "Aa1"
        def password = (longBase * 43)

        when:
        Password.fromPlaintext(password)

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when password does not match regex"() {
        when:
        Password.fromPlaintext("aaaaaaaa")

        then:
        thrown(IllegalValueException)
    }

    def "should create a valid password"() {
        expect:
        Password.fromPlaintext("StrongPass1")
    }

    def "should trim whitespaces"() {
        expect:
        new PlaintextPassword("  StrongPass1  ").value == "StrongPass1"
    }

    def "should reject password containing whitespace"() {
        when:
        Password.fromPlaintext("Strong Pass1")

        then:
        thrown(IllegalValueException)
    }
}
