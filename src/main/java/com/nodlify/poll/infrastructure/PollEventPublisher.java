package com.nodlify.poll.infrastructure;

import com.nodlify.poll.application.PollActivityEvent;
import com.nodlify.shared.web.RequestDetails;
import com.nodlify.shared.web.RequestDetailsResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class PollEventPublisher {

    private final ApplicationEventPublisher applicationEvents;

    public void publish(String action, Authentication authentication, Map<String, String> attributes) {
        var request = resolveRequestDetails(authentication);
        applicationEvents.publishEvent(new PollActivityEvent(
                action,
                userId(authentication),
                request.clientIp(),
                request.userAgent(),
                attributes
        ));
    }

    private RequestDetails resolveRequestDetails(Authentication authentication) {
        if (authentication != null && authentication.getDetails() instanceof RequestDetails details) {
            return details;
        }
        return RequestDetailsResolver.currentRequestDetails();
    }

    private String userId(Authentication authentication) {
        return isAuthenticated(authentication) ? authentication.getName() : null;
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
