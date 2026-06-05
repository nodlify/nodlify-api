package com.nodlify.iam.infrastructure;

import com.nodlify.iam.domain.KeyProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Data
@Configuration
@ConfigurationProperties("nodlify.keys")
@NoArgsConstructor
class KeyPropertiesConfig {

    private String rememberMeKey;

    @Bean
    public KeyProperties keyProperties() {
        log.trace("Creating KeyProperties with rememberMeKey: {}", rememberMeKey);
        return new KeyProperties(rememberMeKey);
    }
}
