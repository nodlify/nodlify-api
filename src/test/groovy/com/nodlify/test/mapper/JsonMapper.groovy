package com.nodlify.test.mapper

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import groovy.json.JsonSlurper
import org.springframework.mock.web.MockHttpServletResponse

trait JsonMapper {

    static def objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT)

    String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj)
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error in converting object to json", e)
        }
    }

    <T> T toObject(String json, Class<T> aClass) {
        try {
            return objectMapper.readValue(json, aClass)
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error in converting json to object", e)
        }
    }

    static Map<String, String> toMap(MockHttpServletResponse response) {
        return new JsonSlurper().parseText(response.contentAsString) as Map
    }
}
