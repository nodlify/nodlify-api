package com.nodlify.test.type

import com.nodlify.test.config.TestConfig
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

@Import(TestConfig)
@SpringBootTest
class SpringBootSpec extends Specification {
}
