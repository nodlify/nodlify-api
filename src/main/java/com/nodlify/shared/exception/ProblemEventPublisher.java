package com.nodlify.shared.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "nodlify.observability.problem.enabled", havingValue = "true")
public class ProblemEventPublisher {

    private final ApplicationEventPublisher applicationEvents;

    public void publish(ProblemCapturedEvent event) {
        applicationEvents.publishEvent(event);
    }
}
