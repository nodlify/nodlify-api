package com.nodlify

import com.nodlify.test.type.SpringBootSpec


class AppSpec extends SpringBootSpec {

    def contextLoads() {
        expect:
        assert appLoaded()
    }

    def appLoaded() {
        true
    }
}
