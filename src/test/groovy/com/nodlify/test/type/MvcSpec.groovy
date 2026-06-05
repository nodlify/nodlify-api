package com.nodlify.test.type

import groovy.transform.CompileStatic
import org.spockframework.runtime.model.parallel.ExecutionMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Execution
import spock.lang.Specification

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup


@WebMvcTest
@CompileStatic
@Execution(ExecutionMode.SAME_THREAD)
class MvcSpec extends Specification {

    @Autowired
    private WebApplicationContext context

    protected MockMvc mockMvc

    void setup() {
        mockMvc = webAppContextSetup(context).build()
    }
}
