package com.nodlify.poll.domain;

import com.nodlify.shared.domain.Value;
import com.nodlify.shared.validation.Assert;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.time.ZoneId;
import java.util.Objects;
import java.util.Set;


public class Timezone implements Value<String> {

    @Getter
    private String value;

    private static final Set<String> TIMEZONES = ZoneId.getAvailableZoneIds();

    Timezone() {
    }

    private Timezone(String value) {
        this.value = value;
    }

    public static Timezone of(String value) {
        var newVal = value == null ? null : value.strip();
        validate(newVal);
        return new Timezone(newVal);
    }

    private static void validate(@Nullable String value) {
        Assert.that("timezone", value)
                .isNotBlank("timezone must not be blank or null")
                .matches(TIMEZONES::contains, "timezone must be one of: " + String.join(", ", TIMEZONES) + "\n");
    }

    @Override
    public @Nonnull String toString() {
        return value == null ? "null" : value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Timezone that)) return false;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
