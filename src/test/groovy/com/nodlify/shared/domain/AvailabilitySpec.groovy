package com.nodlify.shared.domain

import com.nodlify.poll.domain.Availability
import com.nodlify.shared.exception.IllegalValueException
import spock.lang.Specification

class AvailabilitySpec extends Specification {

    def "should throw exception when value is null"() {
        when:
        Availability.from(null)

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when value is blank"() {
        when:
        Availability.from("")

        then:
        thrown(IllegalValueException)
    }

    def "should throw exception when value is invalid"() {
        when:
        Availability.from("invalid")

        then:
        thrown(IllegalValueException)
    }

    def "should parse case-insensitively"() {
        expect:
        Availability.from("yes") == Availability.YES
        Availability.from("No") == Availability.NO
        Availability.from("MaYbE") == Availability.MAYBE
    }

    def "should trim whitespaces"() {
        expect:
        Availability.from("  yes  ") == Availability.YES
    }
}

