package com.nodlify.poll.domain

import com.nodlify.shared.exception.IllegalValueException
import spock.lang.Specification

import java.time.Instant

import static com.nodlify.poll.domain.ChoiceType.MULTIPLE
import static com.nodlify.poll.domain.ChoiceType.SINGLE
import static com.nodlify.poll.domain.PollType.SIMPLE
import static com.nodlify.poll.domain.PollType.TIME

class OptionCompatibilitySpec extends Specification {

    static TimeRange range() {
        new TimeRange(Instant.parse("2030-01-01T10:00:00Z"), Instant.parse("2030-01-01T11:00:00Z"))
    }

    def "TIME poll accepts time options and rejects text options"() {
        given:
        def poll = Poll.from(Title.of("Lunch time"), Description.EMPTY, null, false, TIME, MULTIPLE)

        when:
        poll.addOption(TimeOption.of(range()))

        then:
        poll.options.size() == 1

        when:
        poll.addOption(TextOption.of("Pizza"))

        then:
        thrown(IllegalValueException)
    }

    def "SIMPLE poll accepts text options and rejects time options"() {
        given:
        def poll = Poll.from(Title.of("Pick a meal"), Description.EMPTY, null, false, SIMPLE, SINGLE)

        when:
        poll.addOption(TextOption.of("Pizza"))
        poll.addOption(TextOption.of("Sushi"))

        then:
        poll.options.size() == 2

        when:
        poll.addOption(TimeOption.of(range()))

        then:
        thrown(IllegalValueException)
    }
}
