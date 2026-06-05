package com.nodlify.shared.exception;


public record ProblemCapturedEvent(
        Problem problem,
        String stackTrace
) {
}
