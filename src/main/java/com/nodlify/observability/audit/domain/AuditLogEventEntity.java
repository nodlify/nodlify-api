package com.nodlify.observability.audit.domain;

import com.nodlify.shared.domain.Identifier;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

import static org.hibernate.type.SqlTypes.JSON;


@With
@Getter
@Entity
@Table(name = "audit_log_events")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AuditLogEventEntity {

    @EmbeddedId
    private Identifier id = Identifier.generate();

    @Column(nullable = false)
    private Action action;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "remote_address")
    private String remoteAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(JSON)
    @Column(columnDefinition = "jsonb")
    private AuditPayload payload;

    @CreatedDate
    private Instant createdAt;

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
