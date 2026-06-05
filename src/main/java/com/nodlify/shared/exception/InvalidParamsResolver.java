package com.nodlify.shared.exception;


import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;


final class InvalidParamsResolver {

    private InvalidParamsResolver() {
    }

    static List<InvalidParam> resolveInvalidParams(Exception ex) {
        if (ex instanceof BindException bindException) {
            return extractInvalidParams(bindException);
        }
        return emptyList();
    }

    static String resolveDetail(List<InvalidParam> invalidParams) {
        return invalidParams.stream()
                .map(entries -> entries.getName() + ": " + entries.getReason())
                .collect(Collectors.joining(", "));
    }

    private static List<InvalidParam> extractInvalidParams(BindException exception) {
        return exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .filter(objectError -> StringUtils.isNotBlank(objectError.getDefaultMessage()))
                .map(InvalidParamsResolver::toInvalidParam)
                .filter(Objects::nonNull)
                .toList();
    }

    private static InvalidParam toInvalidParam(ObjectError objectError) {
        try {

            if (!(objectError instanceof FieldError fieldError)) {
                return null;
            }

            var message = objectError.getDefaultMessage();
            var value = fieldError.getRejectedValue();

            return InvalidParam.builder()
                    .name(fieldError.getField())
                    .value(value == null ? null : value.toString())
                    .reason(message)
                    .build();

        } catch (Exception ignored) {
            return null;
        }
    }
}
