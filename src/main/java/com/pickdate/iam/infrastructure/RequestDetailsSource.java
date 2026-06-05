package com.pickdate.iam.infrastructure;

import com.pickdate.shared.web.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationDetailsSource;


@Slf4j
class RequestDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, RequestDetails> {

    @Override
    public @NonNull RequestDetails buildDetails(@NonNull HttpServletRequest request) {
        String ip = resolve(request);
        String userAgent = getUserAgent(request);
        return new RequestDetails(ip, userAgent);
    }

    private static String resolve(HttpServletRequest request) {
        String header = request.getHeader("X-Forwarded-For");
        if (header != null && !header.isBlank()) {
            String[] ips = header.split(",");
            var ip = ips[0].trim();
            log.trace("resolved ip address from X-Forwarded-For: {}", ip);
            return ip;
        }

        String fallback = request.getHeader("X-Real-IP");
        if (fallback != null && !fallback.isBlank()) {
            var ip = fallback.trim();
            log.trace("resolved ip address from X-Real-IP: {}", ip);
            return ip;
        }

        log.trace("resolved ip address from remote address fallback: {}", request.getRemoteAddr());
        return request.getRemoteAddr();
    }

    private static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        log.trace("resolved user agent: {}", userAgent);
        return userAgent;
    }
}
