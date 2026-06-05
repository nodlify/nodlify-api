package com.nodlify.poll.domain


import spock.lang.Specification

class GeoLocationSpec extends Specification {

    def "should create geo location from location data"() {
        given:
        def locationData = new LocationDetails(52.2297d, 21.0122d, "place-id", "Warsaw")

        when:
        def geoLocation = GeoLocation.of(locationData)

        then:
        geoLocation.locationData() == locationData
    }

    def "should create geo location with normalized location data"() {
        when:
        def geoLocation = GeoLocation.of(new LocationDetails(52.0d, 21.0d, "  place-id  ", "  Aleje Jerozolimskie  "))

        then:
        geoLocation.locationData().latitude() == 52.0d
        geoLocation.locationData().longitude() == 21.0d
        geoLocation.locationData().placeId() == "place-id"
        geoLocation.locationData().address() == "Aleje Jerozolimskie"
    }
}
