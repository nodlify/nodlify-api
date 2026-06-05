package com.nodlify.test.type

import com.nodlify.test.config.TestConfig
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import spock.lang.Specification


@DataJpaTest
@Import(TestConfig)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DataJpaSpec extends Specification {
}
