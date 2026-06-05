package com.nodlify.geocoding.infrastructure;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;


@Data
@Configuration
@ConfigurationProperties("nodlify.geocoding")
@NoArgsConstructor
class GeocodingConfig {

    private String baseUrl = "https://nominatim.openstreetmap.org";

    private String userAgent = "nodlify";

    private int searchLimit = 5;

    private boolean rateLimitEnabled = true;

    private int rateLimitMaxRequests = 60;

    private Duration rateLimitWindow = Duration.ofMinutes(1);

    @Bean
    RestClient geocodingRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .requestInterceptor(new GeocodingLoggingInterceptor())
                .defaultHeader(HttpHeaders.USER_AGENT, userAgent)
                .defaultHeader(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .build();
    }
}
