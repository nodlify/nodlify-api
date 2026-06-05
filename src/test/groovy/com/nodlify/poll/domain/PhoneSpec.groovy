package com.nodlify.poll.domain

import com.nodlify.shared.exception.IllegalValueException
import spock.lang.Specification

class PhoneSpec extends Specification {

    def "should throw exception when phone is null"() {
        when:
        Phone.of(null)

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when phone is blank"() {
        when:
        Phone.of("")

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when phone is too short"() {
        given:
        def phone = "123456"

        when:
        Phone.of(phone)

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when phone is too long"() {
        given:
        def phone = ("1" * 33)

        when:
        Phone.of(phone)

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when phone does not match regex"() {
        when:
        Phone.of("12A3456")

        then:
        thrown(IllegalValueException)
    }

    def "should create a valid phone"() {
        expect:
        Phone.of("+48 123-456-789")
    }

    def "should trim whitespaces"() {
        expect:
        Phone.of("  1234567  ").value == "1234567"
    }

    def "should create empty phone object"() {
        expect:
        Phone.ofNullable(null) == Phone.EMPTY
    }
}
