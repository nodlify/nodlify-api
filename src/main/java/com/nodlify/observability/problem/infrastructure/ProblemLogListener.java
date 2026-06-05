package com.nodlify.observability.problem.infrastructure;

import com.nodlify.observability.problem.application.ProblemLogUseCase;
import com.nodlify.shared.exception.ProblemCapturedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "nodlify.observability.problem.enabled", havingValue = "true")
class ProblemLogListener {

    private final ProblemLogUseCase problemLogUseCase;
    private final ProblemConfig problemConfig;

    @Async
    @EventListener
    @Transactional
    public void onProblemEvent(ProblemCapturedEvent problemEvent) {
        if (shouldLog(problemEvent)) {
            log.debug("Handling problem event: {}", problemEvent);
            var entity = ProblemLogEventMapper.toEntity(problemEvent);
            problemLogUseCase.save(entity);
        } else {
            log.debug("Ignoring problem event: {}", problemEvent);
        }
    }

    private boolean shouldLog(ProblemCapturedEvent problemEvent) {
        Integer statusCode = problemEvent.problem().getStatus();
        return problemConfig.loggedStatusesContains(statusCode);
    }
}

