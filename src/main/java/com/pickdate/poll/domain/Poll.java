package com.pickdate.poll.domain;

import com.pickdate.shared.domain.Identifier;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private boolean requireParticipantNames = true;

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

    public static Poll from(Title title, Description description, Instant votingDeadline, boolean requireParticipantNames) {
        var poll = from(title, description);
        poll.votingDeadline = votingDeadline;
        poll.requireParticipantNames = requireParticipantNames;
        return poll;
    }

    public void updateDetails(Title title, Description description) {
        this.title = title;
        this.description = description;
    }

    public void addOption(TimeRange timeRange, boolean wholeDay) {
        var option = Option.from(timeRange, wholeDay);
        options.add(option);
    }

    public void addOption(TimeRange timeRange) {
        var option = Option.from(timeRange);
        options.add(option);
    }

    public void addOption(Option option) {
        options.add(option);
    }

    public void addOptions(List<Option> options) {
        this.options.addAll(options);
    }

    public void removeOption(Identifier optionId) {
        options.removeIf(option -> option.getId().equals(optionId));
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public void addLocation(GeoLocation geoLocation) {
        this.location = geoLocation;
    }
}
