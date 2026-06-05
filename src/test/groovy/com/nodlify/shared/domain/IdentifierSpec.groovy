package com.nodlify.shared.domain


import com.nodlify.shared.exception.IllegalValueException
import spock.lang.Specification

class IdentifierSpec extends Specification {

    def "should be able to create Identifier from string"() {
        given:
        def str = "test"

        when:
        def identifier = Identifier.of(str)

        then:
        identifier.value == str
    }

    def "should be able to generate random Identifier"() {
        when:
        def identifier = Identifier.generate()

        then:
        identifier.value
    }

    def "should not be able to create Identifier from blank string"() {
        when:
        Identifier.of("")

        then:
        thrown(IllegalValueException)
    }

    def "should not be able to create Identifier from whitespaces"() {
        when:
        Identifier.of("     ")

        then:
        thrown(IllegalValueException)
    }

    def "should not be able to create Identifier from null"() {
        when:
        Identifier.of(null)

        then:
        thrown(IllegalValueException)
    }
}
