package com.nodlify.shared.domain;

import java.io.Serializable;


public record Property<T>(String name, T value) implements Serializable {

    public static <T> Property<T> of(String name, T value) {
        return new Property<>(name, value);
    }
}
