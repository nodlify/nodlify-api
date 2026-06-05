package com.nodlify.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;

import java.net.URI;
import java.util.List;

import static com.nodlify.shared.exception.InvalidParamsResolver.resolveDetail;
import static com.nodlify.shared.exception.InvalidParamsResolver.resolveInvalidParams;
import static org.springframework.http.HttpStatus.*;


public final class ProblemFactory {

    private ProblemFactory() {
    }

    public static Problem notFound(NotFoundException ex, URI uri) {
        return ProblemDetailsWithParameters.builder()
                .title("Resource Not Found")
                .status(NOT_FOUND.value())
                .instance(uri.toString())
                .detail(ex.getDetail())
                .invalidParams(List.of(InvalidParam.builder()
                        .name(ex.getProperty().name())
                        .value(ex.getProperty().value())
                        .reason(ex.getDetail())
                        .build()
                ))
                .build();
    }

    public static Problem notFound(RuntimeException ex, URI uri) {
        return ProblemDetails.builder()
                .title("Resource Not Found")
                .status(NOT_FOUND.value())
                .instance(uri.toString())
                .detail(ex.getMessage())
                .build();
    }

    public static Problem badRequest(IllegalValueException ex, URI uri) {
        return ProblemDetailsWithParameters.builder()
                .title("Validation Error")
                .status(BAD_REQUEST.value())
                .instance(uri.toString())
                .detail(ex.getDetail())
                .invalidParams(ex.getProperties().stream()
                        .map(property -> new InvalidParam(
                                property.name(),
                                property.value(),
                                ex.getDetail()
                        ))
                        .toList()
                )
                .build();
    }

    public static Problem conflict(ResourceAlreadyExistException ex, URI uri) {
        return ProblemDetailsWithParameters.builder()
                .title("Resource Conflict")
                .status(CONFLICT.value())
                .instance(uri.toString())
                .detail(ex.getDetail())
                .invalidParams(List.of(InvalidParam.builder()
                        .name(ex.getProperty().name())
                        .value(ex.getProperty().value())
                        .reason(ex.getDetail())
                        .build()
                ))
                .build();
    }

    public static Problem badRequest(Exception ex, URI uri) {
        var params = resolveInvalidParams(ex);
        var detail = resolveDetail(params);
        return ProblemDetailsWithParameters.builder()
                .title("Validation Error")
                .status(BAD_REQUEST.value())
                .instance(uri.toString())
                .detail(detail)
                .invalidParams(params)
                .build();
    }

    public static Problem internalServerError(InternalServerError ex, URI uri) {
        return ProblemDetails.builder()
                .title("Internal Server Error")
                .status(INTERNAL_SERVER_ERROR.value())
                .instance(uri.toString())
                .detail(ex.getDetail())
                .build();
    }

    public static Problem internalServerError(URI uri) {
        return ProblemDetails.builder()
                .title("Internal Server Error")
                .status(INTERNAL_SERVER_ERROR.value())
                .instance(uri.toString())
                .build();
    }

    public static Problem resolveException(ErrorResponse errorResponse, URI uri) {
        return ProblemDetails.builder()
                .title(((HttpStatus) (errorResponse.getStatusCode())).getReasonPhrase())
                .status(errorResponse.getStatusCode().value())
                .instance(uri.toString())
                .build();
    }
}

