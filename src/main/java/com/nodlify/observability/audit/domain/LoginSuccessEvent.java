package com.nodlify.observability.audit.domain;


public record LoginSuccessEvent(
        String userId,
        String remoteAddress,
        String userAgent
) implements AuditEvent {

    @Override
    public Action action() {
        return Action.of("login_success_event");
    }
}
