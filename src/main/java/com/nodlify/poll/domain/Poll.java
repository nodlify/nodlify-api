package com.nodlify.poll.domain;

import com.nodlify.shared.domain.Identifier;
import com.nodlify.shared.domain.Property;
import com.nodlify.shared.exception.IllegalValueException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.*;

import static jakarta.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;


@Getter
@Entity
@Table(name = "polls")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Poll {

    @With
    @EmbeddedId
    private Identifier id = Identifier.generate();

    private Title title;

    private Description description;

    private Instant votingDeadline;

    @Column(name = "allow_anonymous")
    private boolean allowAnonymous = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PollType type = PollType.TIME;

    @Enumerated(EnumType.STRING)
    @Column(name = "choice_type")
    private ChoiceType choiceType = ChoiceType.MULTIPLE;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PollStatus manualStatus;

    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private GeoLocation location;

    @OneToMany(cascade = ALL, orphanRemoval = true)
    @JoinColumn(
            name = "poll_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Set<Option> options = new HashSet<>();

    @OneToMany(cascade = ALL, orphanRemoval = true)
    @JoinColumn(
            name = "poll_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Set<Participant> participants = new HashSet<>();

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public static Poll from(Title title, Description description) {
        var poll = new Poll();
        poll.title = title;
        poll.description = description;
        return poll;
    }

    public static Poll from(Title title, Description description, Instant votingDeadline) {
        var poll = from(title, description);
        poll.votingDeadline = votingDeadline;
        return poll;
    }

    public static Poll from(Title title, Description description, Instant votingDeadline, boolean allowAnonymous) {
        var poll = from(title, description, votingDeadline);
        poll.allowAnonymous = allowAnonymous;
        return poll;
    }

    public static Poll from(
            Title title,
            Description description,
            Instant votingDeadline,
            boolean allowAnonymous,
            PollType type,
            ChoiceType choiceType
    ) {
        var poll = from(title, description, votingDeadline, allowAnonymous);
        poll.type = type == null ? PollType.TIME : type;
        poll.choiceType = choiceType == null ? ChoiceType.MULTIPLE : choiceType;
        return poll;
    }

    public void updateDetails(Title title, Description description) {
        this.title = title;
        this.description = description;
    }

    public void addOption(TimeRange timeRange, boolean wholeDay) {
        addOption(TimeOption.of(timeRange, wholeDay));
    }

    public void addOption(TimeRange timeRange) {
        addOption(TimeOption.of(timeRange));
    }

    public void addOption(Option option) {
        ensureCompatible(option);
        options.add(option);
    }

    public void addOptions(List<Option> options) {
        options.forEach(this::addOption);
    }

    private void ensureCompatible(Option option) {
        var expected = type == PollType.SIMPLE ? TextOption.class : TimeOption.class;
        if (!expected.isInstance(option)) {
            throw new IllegalValueException(
                    Property.of("option", option.getClass().getSimpleName()),
                    "Option type does not match poll type " + type);
        }
    }

    public void removeOption(Identifier optionId) {
        options.removeIf(option -> option.getId().equals(optionId));
    }

    public Participant addParticipant(Participant participant) {
        var userId = participant.getUserId();
        if (userId != null) {
            var existing = participants.stream()
                    .filter(item -> userId.equals(item.getUserId()))
                    .findFirst();
            if (existing.isPresent()) {
                return existing.get();
            }
        }
        var email = participant.getEmail();
        if (email != null) {
            var existing = participants.stream()
                    .filter(item -> email.equals(item.getEmail()))
                    .findFirst();
            if (existing.isPresent()) {
                return existing.get();
            }
        }
        participants.add(participant);
        return participant;
    }

    public Optional<Participant> findParticipant(Identifier userId) {
        return participants.stream()
                .filter(item -> userId.equals(item.getUserId()))
                .findFirst();
    }

    public void addLocation(GeoLocation geoLocation) {
        this.location = geoLocation;
    }

    public void changeStatus(PollStatus status) {
        this.manualStatus = status == PollStatus.VOTING ? null : status;
    }

    public PollStatus status(Instant now) {
        if (manualStatus != null) {
            return manualStatus;
        }
        return isPastDeadline(now) ? PollStatus.CLOSED : PollStatus.VOTING;
    }

    private boolean isPastDeadline(Instant now) {
        var deadline = votingDeadline != null ? votingDeadline : lastOptionEnd();
        return deadline != null && now.isAfter(deadline);
    }

    private Instant lastOptionEnd() {
        return options.stream()
                .filter(TimeOption.class::isInstance)
                .map(TimeOption.class::cast)
                .map(TimeOption::getEndAt)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }
}
