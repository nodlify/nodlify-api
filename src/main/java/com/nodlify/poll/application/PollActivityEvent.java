package com.nodlify.poll.application;

import java.util.Map;


public record PollActivityEvent(
        String action,
        String userId,
        String remoteAddress,
        String userAgent,
        Map<String, String> attributes
) {
}
