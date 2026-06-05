package com.nodlify.shared.domain


import com.nodlify.shared.exception.IllegalValueException
import spock.lang.Specification

class EmailSpec extends Specification {

    def "should normalize email to lowercase so different letter case results in the same value"() {
        when:
        def lower = Email.of("test@example.com")
        def upped = Email.of("TEST@EXAMPLE.COM")

        then:
        lower.value == "test@example.com"
        upped.value == "test@example.com"
        lower == upped
    }

    def "should throw exception when email is null"() {
        when:
        Email.of(null)

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when email is blank"() {
        when:
        Email.of("")

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when email is too short"() {
        given:
        def email = "a@a"

        when:
        Email.of(email)

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when email is too long"() {
        given:
        def email = "a" * 255

        when:
        Email.of(email)

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when email does not match regex"() {
        when:
        Email.of("invalid-email@")

        then:
        thrown(IllegalValueException)
    }

    def "should create a valid email"() {
        expect:
        Email.of("test.email-1_2@example-domain.com")
    }

    def "should trim whitespaces"() {
        expect:
        Email.of("test@example.com  ").value == "test@example.com"
    }

    def "should create empty email object"() {
        expect:
        Email.ofNullable(null) == Email.EMPTY
    }
}
