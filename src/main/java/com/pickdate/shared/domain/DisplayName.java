package com.pickdate.shared.domain;

import com.pickdate.shared.validation.Assert;
import jakarta.annotation.Nonnull;
import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;


public class DisplayName implements Value<String> {

    public static final DisplayName EMPTY = new DisplayName();

    private static final Pattern pattern = Pattern.compile("^[\\p{L}\\p{M}0-9][\\p{L}\\p{M}0-9 ._\\-']*$");

    @Getter
    private String value;

    DisplayName() {
    }

    private DisplayName(String value) {
        this.value = value;
    }

    public static DisplayName of(String value) {
        var newValue = value == null ? null : value.strip();
        validate(newValue);
        return new DisplayName(newValue);
    }

    public static DisplayName ofNullable(String value) {
        return value == null ? EMPTY : of(value);
    }

    private static void validate(String value) {
        Assert.that("displayName", value)
                .isNotBlank("displayName must not be blank")
                .hasMinLength(2, "displayName must be at least 2 characters long")
                .hasMaxLength(50, "displayName must be at most 50 characters long")
                .matches(pattern, "displayName contains unsupported characters");
    }

    @Override
    public @Nonnull String toString() {
        return value == null ? "null" : value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof DisplayName that)) return false;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
