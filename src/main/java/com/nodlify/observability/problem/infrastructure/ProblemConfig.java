package com.nodlify.observability.problem.infrastructure;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;


@Slf4j
@Data
@Configuration
@ConfigurationProperties("nodlify.observability.problem")
@NoArgsConstructor
class ProblemConfig {

    boolean enabled;

    Set<Integer> loggedStatuses;

    @PostConstruct
    void init() {
        log.info("Problem logging is {}", enabled ? "enabled" : "disabled");
        if (enabled) {
            log.info("Logged statuses are {}", loggedStatuses);
        }
    }

    boolean loggedStatusesContains(int status) {
        return loggedStatuses.contains(status);
    }
}
