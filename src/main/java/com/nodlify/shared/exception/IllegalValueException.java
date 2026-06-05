package com.nodlify.shared.exception;

import com.nodlify.shared.domain.Property;
import lombok.Getter;

import java.util.List;


@Getter
public class IllegalValueException extends RuntimeException {

    private final List<Property<?>> properties;
    private final String detail;

    public IllegalValueException(List<Property<?>> properties, String detail) {
        detail = detail == null ? "" : detail;
        this.properties = properties;
        this.detail = detail;
        super(detail);
    }

    public IllegalValueException(Property<?> property, String detail) {
        this(List.of(property), detail);
    }
}
