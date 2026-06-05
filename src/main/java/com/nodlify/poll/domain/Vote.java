package com.nodlify.poll.domain;

import com.nodlify.shared.domain.Identifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;


@Getter
@Entity
@Table(name = "votes")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Vote {

    @EmbeddedId
    private VoteId id;

    @Column(name = "poll_id")
    private Identifier pollId;

    @Enumerated(STRING)
    private Availability availability;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public Vote with(VoteId id) {
        this.id = id;
        return this;
    }

    public Vote with(Identifier pollId) {
        this.pollId = pollId;
        return this;
    }

    public Vote with(Availability availability) {
        this.availability = availability;
        return this;
    }

    public Identifier getParticipantId() {
        return id.participantId;
    }

    public Identifier getOptionId() {
        return id.optionId;
    }

    @Data
    @NoArgsConstructor(access = PROTECTED)
    @AllArgsConstructor
    public static class VoteId implements Serializable {

        @Column(name = "participant_id")
        private Identifier participantId;

        @Column(name = "option_id")
        private Identifier optionId;
    }
}
