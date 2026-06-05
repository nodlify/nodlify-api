package com.nodlify.observability.audit.infrastructure

import com.nodlify.shared.web.RequestDetails
import com.nodlify.test.fixture.AuthenticationFixture
import spock.lang.Specification

class RequestDetailsExtractorSpec extends Specification {

    def "should extract ip from request details"() {
        given:
        def authentication = AuthenticationFixture.authentication("john", new RequestDetails("127.0.0.1", "Firefox"))

        expect:
        RequestDetailsExtractor.extractIp(authentication) == "127.0.0.1"
    }

    def "should extract user agent from request details"() {
        given:
        def authentication = AuthenticationFixture.authentication("john", new RequestDetails("127.0.0.1", "Firefox"))

        expect:
        RequestDetailsExtractor.extractUserAgent(authentication) == "Firefox"
    }
}

