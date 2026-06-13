package com.nodlify.observability.audit.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public interface AuditPayload extends Serializable {
}
