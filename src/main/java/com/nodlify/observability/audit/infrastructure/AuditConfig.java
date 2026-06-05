package com.nodlify.observability.audit.infrastructure;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Data
@Configuration
@ConfigurationProperties("nodlify.observability.audit")
@NoArgsConstructor
class AuditConfig {

    private boolean enabled;
    private boolean extractIp;
    private boolean extractUserAgent;

    @PostConstruct
    void init() {
        log.info("Audit logging is {}", enabled ? "enabled" : "disabled");
        if (enabled) {
            log.info("Extracting IP is {}", extractIp ? "enabled" : "disabled");
            log.info("Extracting User Agent is {}", extractUserAgent ? "enabled" : "disabled");
        }
    }
}
