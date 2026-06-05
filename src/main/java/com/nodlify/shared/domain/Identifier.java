package com.nodlify.shared.domain;

import com.nodlify.shared.validation.Assert;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;

import static java.util.UUID.randomUUID;


public class Identifier implements Value<String> {

    private String id;

    Identifier() {
    }

    private Identifier(String id) {
        this.id = id;
    }

    public static Identifier of(String value) {
        var id = value == null ? null : value.strip();
        validate(id);
        return new Identifier(id);
    }

    public static Identifier generate() {
        return new Identifier(randomUUID().toString());
    }

    private static void validate(@Nullable String value) {
        Assert.that("id", value)
                .isNotBlank("id cannot be blank or null");
    }

    @Override
    public String getValue() {
        return id;
    }

    @Override
    public @Nonnull String toString() {
        return id == null ? "null" : id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Identifier that)) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
