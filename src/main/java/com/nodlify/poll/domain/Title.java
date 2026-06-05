package com.nodlify.poll.domain;

import com.nodlify.shared.domain.Value;
import com.nodlify.shared.validation.Assert;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.Objects;


public class Title implements Value<String> {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 255;

    @Getter
    private String value;

    Title() {
    }

    private Title(String value) {
        this.value = value;
    }

    public static Title of(String value) {
        var newValue = value == null ? null : value.strip();
        validate(newValue);
        return new Title(newValue);
    }

    public static void validate(@Nullable String value) {
        Assert.that("title", value)
                .isNotBlank("title must not be blank")
                .hasMinLength(MIN_LENGTH, "title must be at least " + MIN_LENGTH + " characters long")
                .hasMaxLength(MAX_LENGTH, "title must be at most " + MAX_LENGTH + " characters long");
    }

    @Override
    public @Nonnull String toString() {
        return value == null ? "null" : value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Title that)) return false;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
