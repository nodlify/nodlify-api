package com.nodlify.observability.audit.domain;

import com.nodlify.shared.domain.Value;
import com.nodlify.shared.validation.Assert;
import jakarta.annotation.Nonnull;
import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;


public class Action implements Value<String> {

    public static final Action EMPTY = new Action();

    private static final Pattern PATTERN = Pattern.compile("^[a-z]+(?:_[a-z]+)*$"); // NOSONAR

    @Getter
    private String value;

    Action() {
    }

    private Action(String value) {
        this.value = value;
    }

    public static Action of(String value) {
        validate(value);
        return new Action(value);
    }

    private static void validate(String value) {
        Assert.that("activity", value)
                .isNotBlank("activity must not be null or blank")
                .hasMaxLength(100, "activity must be at most 100 characters long")
                .matches(PATTERN, "action must be lowercase words separated by single underscores");
    }

    public static Action ofNullable(String action) {
        if (action == null || action.isBlank()) return EMPTY;
        return Action.of(action);
    }

    @Override
    public @Nonnull String toString() {
        return value == null ? "null" : value;
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Action action)) return false;
        return Objects.equals(value, action.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
