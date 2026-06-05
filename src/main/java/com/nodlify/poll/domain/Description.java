package com.nodlify.poll.domain;

import com.nodlify.shared.domain.Value;
import com.nodlify.shared.validation.Assert;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.Objects;


public class Description implements Value<String> {

    public static final Description EMPTY = new Description();

    @Getter
    private String value;

    Description() {
    }

    private Description(String value) {
        this.value = value;
    }

    public static Description of(String value) {
        var newVal = value == null ? null : value.strip();
        validate(newVal);
        return new Description(newVal);
    }

    public static Description ofNullable(String value) {
        return value == null ? EMPTY : of(value);
    }

    private static void validate(@Nullable String value) {
        Assert.that("description", value)
                .isNotBlank("description must not be null or blank");
    }

    @Override
    public @Nonnull String toString() {
        return value == null ? "null" : value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Description that)) return false;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
