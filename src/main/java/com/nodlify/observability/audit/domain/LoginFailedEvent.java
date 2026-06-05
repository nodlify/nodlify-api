package com.nodlify.observability.audit.domain;


public record LoginFailedEvent(
        String userId,
        String remoteAddress,
        String userAgent
) implements AuditEvent {

    @Override
    public Action action() {
        return Action.of("login_failed_event");
    }
}
