package com.nodlify.shared.domain;

import com.nodlify.shared.validation.Assert;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;


public class Email implements Value<String> {

    public static final Email EMPTY = new Email();

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 254;

    // https://regex101.com/r/JWNwl0/3
    private static final Pattern PATTERN = compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}$");

    @Getter
    private String value;

    Email() {
    }

    private Email(String value) {
        this.value = value;
    }

    public static @NonNull Email of(String value) {
        var newValue = value == null ? null : normalize(value);
        validate(newValue);
        return new Email(newValue);
    }

    public static @NonNull Email ofNullable(String value) {
        return value == null ? EMPTY : of(value);
    }

    private static void validate(@Nullable String value) {
        Assert.that("email", value)
                .isNotBlank("Email cannot be null or blank")
                .hasMinLength(MIN_LENGTH, "Email must be at least " + MIN_LENGTH + " characters long")
                .hasMaxLength(MAX_LENGTH, "Email must be at most " + MAX_LENGTH + " characters long")
                .matches(PATTERN, "Invalid email format");
    }

    private static @NonNull String normalize(String value) {
        return value.strip().toLowerCase();
    }

    @Override
    public @Nonnull String toString() {
        return value == null ? "null" : value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Email email)) return false;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
