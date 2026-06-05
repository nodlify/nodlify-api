package com.nodlify.poll.domain

import spock.lang.Specification


class LocationDetailsSpec extends Specification {

    def "should create location data with normalized optional fields"() {
        when:
        def locationData = new LocationDetails(52.2297d, 21.0122d, "  place-id  ", "  Warsaw  ")

        then:
        locationData.latitude() == 52.2297d
        locationData.longitude() == 21.0122d
        locationData.placeId() == "place-id"
        locationData.address() == "Warsaw"
    }

    def "should keep null optional fields"() {
        when:
        def locationData = new LocationDetails(52.2297d, 21.0122d, null, null)

        then:
        locationData.placeId() == null
        locationData.address() == null
    }

    def "should normalize blank optional fields to empty strings"() {
        when:
        def locationData = new LocationDetails(52.2297d, 21.0122d, "   ", "\t  ")

        then:
        locationData.placeId() == ""
        locationData.address() == ""
    }

    def "should accept boundary coordinates latitude=#latitude longitude=#longitude"() {
        expect:
        new LocationDetails(latitude, longitude, null, null)

        where:
        latitude | longitude
        -90.0d   | -180.0d
        -90.0d   | 180.0d
        90.0d    | -180.0d
        90.0d    | 180.0d
        0.0d     | 0.0d
    }
}
