package com.nodlify.poll.domain

import spock.lang.Specification

import java.time.Instant

import static com.nodlify.poll.domain.PollStatus.*

class PollStatusSpec extends Specification {

    static final Instant NOW = Instant.parse("2026-06-08T12:00:00Z")

    def poll(Instant deadline, List<Instant> optionEnds) {
        def p = new Poll()
                .withTitle(Title.of("Trip"))
                .withDescription(Description.of("desc"))
                .withVotingDeadline(deadline)
        optionEnds.each { end ->
            p.addOption(TimeOption.of(new TimeRange(end.minusSeconds(3600), end), false))
        }
        return p
    }

    def "is VOTING when there is no deadline and no options"() {
        expect:
        poll(null, []).status(NOW) == VOTING
    }

    def "falls back to the last option end when no deadline is set"() {
        expect:
        poll(null, [past]).status(NOW) == CLOSED
        poll(null, [future]).status(NOW) == VOTING
        poll(null, [past, future]).status(NOW) == VOTING

        where:
        past = Instant.parse("2026-06-01T10:00:00Z")
        future = Instant.parse("2026-12-01T10:00:00Z")
    }

    def "uses the voting deadline when set, regardless of options"() {
        expect:
        poll(Instant.parse("2026-06-07T10:00:00Z"), [Instant.parse("2026-12-01T10:00:00Z")]).status(NOW) == CLOSED
        poll(Instant.parse("2026-12-31T10:00:00Z"), [Instant.parse("2026-01-01T10:00:00Z")]).status(NOW) == VOTING
    }

    def "manual status overrides the time-based one"() {
        given:
        def p = poll(null, [Instant.parse("2026-01-01T10:00:00Z")]) // past -> CLOSED by time

        when:
        p.changeStatus(DECIDED)

        then:
        p.status(NOW) == DECIDED
    }

    def "reopening clears the manual override and falls back to time"() {
        given:
        def p = poll(Instant.parse("2026-12-31T10:00:00Z"), []) // future deadline -> VOTING
        p.changeStatus(CLOSED)

        expect:
        p.status(NOW) == CLOSED

        when:
        p.changeStatus(VOTING)

        then:
        p.status(NOW) == VOTING
    }
}
