package com.nodlify.shared.exception;

import com.nodlify.shared.domain.Property;
import lombok.Getter;


@Getter
public class ResourceAlreadyExistException extends RuntimeException {

    private final Property<Object> property;
    private final String detail;

    public ResourceAlreadyExistException(Property<Object> property, String detail) {
        detail = detail == null ? "" : detail;
        this.property = property;
        this.detail = detail;
        super(detail);
    }
}
