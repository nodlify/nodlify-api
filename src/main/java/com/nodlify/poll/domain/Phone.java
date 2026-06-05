package com.nodlify.poll.domain;

import com.nodlify.shared.domain.Value;
import com.nodlify.shared.validation.Assert;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;
import java.util.regex.Pattern;


public class Phone implements Value<String> {

    public static final Phone EMPTY = new Phone();

    private static final Pattern PHONE_REGEX = Pattern.compile("^[0-9 +\\-()]{7,50}$");

    private String value;

    Phone() {
    }

    private Phone(String value) {
        this.value = value;
    }

    public static Phone of(String value) {
        var newVal = value == null ? null : value.strip();
        validate(newVal);
        return new Phone(newVal);
    }

    public static @Nullable Phone ofNullable(String value) {
        return value == null ? EMPTY : of(value);
    }

    private static void validate(@Nullable String value) {
        Assert.that("phone", value)
                .isNotBlank("phone must not be null or blank")
                .hasMinLength(7, "phone must be at least 7 characters long")
                .hasMaxLength(32, "phone must be at most 32 characters long")
                .matches(PHONE_REGEX, "phone may contain only digits, spaces, '+', '-', '(' or ')'");
    }

    @Override
    public @Nonnull String toString() {
        return value == null ? "null" : value;
    }

    @Override
    public @Nullable String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Phone that)) return false;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
