package com.nodlify.poll.domain;

import com.nodlify.shared.validation.Assert;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import static com.nodlify.shared.validation.Matchers.oneOf;
import static java.util.Objects.isNull;

public enum Availability {

    YES, NO, MAYBE;

    public static Availability from(String value) {
        value = isNull(value) ? null : normalize(value);
        validate(value);
        return valueOf(value);
    }

    private static String normalize(String value) {
        return value.strip().toUpperCase();
    }

    private static void validate(@Nullable String value) {
        Assert.that("availability", value)
                .isNotBlank("availability must not be blank or null")
                .matches(oneOf(YES, NO, MAYBE), "availability must be one of: YES, NO, MAYBE");
    }

    @Override
    public @Nonnull String toString() {
        return name();
    }
}
