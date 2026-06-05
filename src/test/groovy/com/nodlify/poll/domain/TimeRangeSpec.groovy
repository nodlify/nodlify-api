package com.nodlify.poll.domain

import com.nodlify.shared.exception.IllegalValueException
import spock.lang.Specification

import java.time.Instant

import static com.nodlify.test.fixture.RangeFixture.instantFrom


class TimeRangeSpec extends Specification {

    def "should be able to create range"() {
        given:
        def startDate = Instant.now()
        def endDate = Instant.now()

        expect:
        new TimeRange(startDate, endDate)
    }

    def "should throw exception when startDate is after endDate"() {
        given:
        def startDate = instantFrom("2025-12-17T00:00:00")
        def endDate = instantFrom("2025-12-16T00:00:00")

        when:
        new TimeRange(startDate, endDate)

        then:
        thrown(IllegalValueException)
    }

    def "should be able to check if range is in the past"() {
        given:
        def startDate = instantFrom("2025-12-16T00:00:00")
        def endDate = instantFrom("2025-12-16T00:00:00")

        when:
        def range = new TimeRange(startDate, endDate)

        then:
        range.isInThePast()
    }
}
