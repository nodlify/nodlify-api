package com.nodlify.shared.domain;

import java.io.Serializable;


public interface Value<T> extends Serializable {

    static <T> T valueOrNull(Value<T> value) {
        if (value == null) return null;
        return value.getValue() == null
                ? null
                : value.getValue();
    }

    T getValue();
}
