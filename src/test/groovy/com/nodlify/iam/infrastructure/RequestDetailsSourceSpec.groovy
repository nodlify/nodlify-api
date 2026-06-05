package com.nodlify.iam.infrastructure

import org.springframework.mock.web.MockHttpServletRequest
import spock.lang.Specification


class RequestDetailsSourceSpec extends Specification {

    def source = new RequestDetailsSource()

    def "should prefer first forwarded address"() {
        given:
        def request = new MockHttpServletRequest()
        request.addHeader("X-Forwarded-For", "203.0.113.10, 10.0.0.1")
        request.addHeader("User-Agent", "curl/8.6.0")
        request.remoteAddr = "::1"

        when:
        def details = source.buildDetails(request)

        then:
        details.clientIp() == "203.0.113.10"
        details.userAgent() == "curl/8.6.0"
    }

    def "should fallback to real ip header"() {
        given:
        def request = new MockHttpServletRequest()
        request.addHeader("X-Real-IP", "198.51.100.7")
        request.remoteAddr = "::1"

        when:
        def details = source.buildDetails(request)

        then:
        details.clientIp() == "198.51.100.7"
    }
}

