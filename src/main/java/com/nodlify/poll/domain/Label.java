package com.nodlify.poll.domain;

import com.nodlify.shared.domain.Value;
import com.nodlify.shared.validation.Assert;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.Objects;


public class Label implements Value<String> {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 255;

    @Getter
    private String value;

    Label() {
    }

    private Label(String value) {
        this.value = value;
    }

    public static Label of(String value) {
        var newValue = value == null ? null : value.strip();
        validate(newValue);
        return new Label(newValue);
    }

    private static void validate(@Nullable String value) {
        Assert.that("label", value)
                .isNotBlank("label must not be blank")
                .hasMinLength(MIN_LENGTH, "label must be at least " + MIN_LENGTH + " character long")
                .hasMaxLength(MAX_LENGTH, "label must be at most " + MAX_LENGTH + " characters long");
    }

    @Override
    public @Nonnull String toString() {
        return value == null ? "null" : value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Label that)) return false;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
