package com.nodlify.poll.domain

import com.nodlify.shared.exception.IllegalValueException
import spock.lang.Specification


class TitleSpec extends Specification {

    def "should create Title for valid input"() {
        given:
        def value = "Board games meeting"

        when:
        def title = Title.of(value)

        then:
        title != null
        title.value == value
    }

    def "should trim input before validation and storage"() {
        when:
        def title = Title.of("   Sprint Review   ")

        then:
        title.value == "Sprint Review"
    }

    def "should not allow blank or null title"() {
        when: "empty string"
        Title.of("")

        then:
        thrown(IllegalValueException)

        when: "whitespaces only"
        Title.of("    \t   ")

        then:
        thrown(IllegalValueException)

        when: "null string"
        Title.of(null)

        then:
        thrown(IllegalValueException)
    }

    def "should enforce minimum length of 3 characters for input"() {
        when:
        Title.of("ab")

        then:
        thrown(IllegalValueException)
    }

    def "should allow exactly min length and max length"() {
        given:
        def minOk = "abc"
        def maxOk = "x" * 255

        expect:
        Title.of(minOk).value == minOk
        Title.of(maxOk).value == maxOk
    }

    def "should reject values exceeding max length"() {
        given:
        def tooLong = "x" * 256

        when:
        Title.of(tooLong)

        then:
        thrown(IllegalValueException)
    }
}
