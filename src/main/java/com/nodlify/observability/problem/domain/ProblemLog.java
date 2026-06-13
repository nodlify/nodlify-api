package com.nodlify.observability.problem.domain;

import com.nodlify.shared.domain.Identifier;
import com.nodlify.shared.exception.InvalidParam;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@With
@Getter
@Entity
@Table(name = "problem_log_events")
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProblemLog {

    @EmbeddedId
    private Identifier id;

    private String title;
    private int status;
    private String detail;
    private String instance;

    @Column(name = "stack_trace")
    private String stackTrace;

    @Type(JsonType.class)
    @Column(name = "invalid_params")
    private List<InvalidParam> invalidParams = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    public ProblemLog() {
        this.id = Identifier.generate();
    }
}
