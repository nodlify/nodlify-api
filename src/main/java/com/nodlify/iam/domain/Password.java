package com.nodlify.iam.domain;

import com.nodlify.shared.domain.Value;
import com.nodlify.shared.validation.Assert;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;


public sealed interface Password extends Value<String> {

    static Password fromPlaintext(String value) {
        return new PlaintextPassword(value);
    }

    static Password hashed(String value) {
        return new HashedPassword(value);
    }

    boolean isHashed();
}

record PlaintextPassword(String value) implements Password {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;

    // At least one lowercase, one uppercase, and either one digit or one special char; no whitespace; length 8-128
    private static final Pattern PATTERN = compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*(?:\\d|[^A-Za-z0-9\\s]))\\S{8,128}$");

    PlaintextPassword(String value) {
        this.value = value == null ? null : value.strip();
        validate(this.value);
    }

    static void validate(@Nullable String value) {
        Assert.that("password", value)
                .isNotBlank("Password cannot be null or blank")
                .hasMinLength(MIN_LENGTH, "Password must be at least " + MIN_LENGTH + " characters long")
                .hasMaxLength(MAX_LENGTH, "Password must be at most " + MAX_LENGTH + " characters long")
                .matches(PATTERN, "Invalid password format");
    }

    @Override
    public boolean isHashed() {
        return false;
    }

    @Override
    public @Nonnull String toString() {
        return value == null ? "null" : value;
    }

    @Override
    public String getValue() {
        return value;
    }
}

record HashedPassword(String value) implements Password {

    private static final Pattern HASHED_PATTERN = compile("^\\{[A-Za-z0-9_-]+\\}\\S+$");

    HashedPassword(String value) {
        this.value = value;
        validate(this.value);
    }

    static void validate(String value) {
        Assert.that("password", value)
                .isNotBlank("Password hash cannot be null or blank")
                .matches(HASHED_PATTERN, "Invalid password hash format");
    }

    @Override
    public boolean isHashed() {
        return HASHED_PATTERN.matcher(value).matches();
    }

    @Override
    public @Nonnull String toString() {
        return value == null ? "null" : value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
