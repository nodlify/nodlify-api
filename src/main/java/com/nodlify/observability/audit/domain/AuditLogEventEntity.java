package com.nodlify.observability.audit.domain;

import com.nodlify.shared.domain.Identifier;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;


@With
@Getter
@Entity
@Table(name = "audit_log_events")
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AuditLogEventEntity {

    @EmbeddedId
    private Identifier id;

    @Column(nullable = false)
    private Action action;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "remote_address")
    private String remoteAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Type(JsonType.class)
    @Column(name = "payload")
    private AuditPayload payload;

    @CreatedDate
    private Instant createdAt;

    public AuditLogEventEntity() {
        this.id = Identifier.generate();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof AuditLogEventEntity that)) return false;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
