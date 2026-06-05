package com.nodlify.observability.audit.application;

import java.time.Instant;


public record AuditLogFilter(
        String action,
        String user,
        Instant from,
        Instant to
) {

    public boolean isEmpty() {
        return action == null
                && user == null
                && from == null
                && to == null;
    }
}
