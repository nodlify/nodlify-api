package com.nodlify.observability.audit.infrastructure;

import com.nodlify.shared.web.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.function.Function;


final class RequestDetailsExtractor {

    private RequestDetailsExtractor() {
    }

    static String extractIp(Authentication authentication) {
        String fromDetails = extractFromDetails(authentication, RequestDetails::clientIp);
        if (fromDetails != null) return fromDetails;
        return extractFromCurrentRequest(RequestDetailsExtractor::resolveIp);
    }

    static String extractUserAgent(Authentication authentication) {
        String fromDetails = extractFromDetails(authentication, RequestDetails::userAgent);
        if (fromDetails != null) return fromDetails;
        return extractFromCurrentRequest(request -> request.getHeader("User-Agent"));
    }

    private static String extractFromDetails(
            Authentication authentication,
            Function<RequestDetails, String> mapper
    ) {
        if (authentication == null) return null;
        Object details = authentication.getDetails();
        return details instanceof RequestDetails requestDetails
                ? mapper.apply(requestDetails)
                : null;
    }

    private static String extractFromCurrentRequest(Function<HttpServletRequest, String> mapper) {
        var attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletAttributes) {
            return mapper.apply(servletAttributes.getRequest());
        }
        return null;
    }

    private static String resolveIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }
}
