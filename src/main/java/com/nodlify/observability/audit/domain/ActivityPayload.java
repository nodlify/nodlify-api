package com.nodlify.observability.audit.domain;

import java.util.Map;


public record ActivityPayload(Map<String, String> attributes) implements AuditPayload {
}
