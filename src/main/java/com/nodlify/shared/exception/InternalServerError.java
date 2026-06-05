package com.nodlify.shared.exception;

import lombok.Getter;


@Getter
public class InternalServerError extends RuntimeException {

    private final String detail;

    public InternalServerError(String detail) {
        detail = detail == null ? "" : detail;
        this.detail = detail;
        super(detail);
    }
}
