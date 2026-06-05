package com.nodlify.geocoding.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
class GeocodingLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution
    ) throws IOException {
        log.debug("Geocoding request → {} {}", request.getMethod(), request.getURI());

        var response = execution.execute(request, body);

        if (log.isDebugEnabled()) {
            var responseBody = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
            log.debug("Geocoding response ← {} {} | body: {}",
                    response.getStatusCode().value(), request.getURI(), responseBody);
        }

        return response;
    }
}
