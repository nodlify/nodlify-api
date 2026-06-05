package com.nodlify

import com.nodlify.test.type.SpringBootSpec
import spock.lang.Requires

@Requires({ it.env['INCLUDE_SLOW_TESTS'] == 'true' })
class AppSpec extends SpringBootSpec {

    def contextLoads() {
        expect:
        assert appLoaded()
    }

    def appLoaded() {
        true
    }
}
