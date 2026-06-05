package com.nodlify.test.fixture

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class RangeFixture {

    static Instant instantFrom(String text = "2025-12-16T17:47:00") {
        LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(ZoneId.of("UTC"))
                .toInstant()
    }
}
