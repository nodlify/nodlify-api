package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.Description;
import com.nodlify.poll.domain.LocationDetails;
import com.nodlify.poll.domain.TimeRange;
import com.nodlify.poll.domain.Title;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
class CreatePollRequest {

    @Size.List({
            @Size(min = 3, message = "{validation.title.size.too_short}"),
            @Size(max = 255, message = "{validation.title.size.too_long}")
    })
    @NotBlank(message = "{validation.title.not_blank}")
    private String title;

    private String description;

    private Instant votingDeadline;

    private Boolean requireParticipantNames = true;

    @Valid
    private LocationPayload location;

    @Valid
    private List<OptionPayload> options;

    Title getTitle() {
        return Title.of(title);
    }

    Description getDescription() {
        return Description.ofNullable(description);
    }

    LocationDetails toLocationDetails() {
        return location == null ? null : location.toLocationDetails();
    }

    boolean isRequireParticipantNames() {
        return requireParticipantNames == null || requireParticipantNames;
    }

    List<OptionPayload> getOptions() {
        return options == null ? List.of() : options;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class LocationPayload {
        private double latitude;
        private double longitude;
        private String placeId;
        private String address;

        LocationDetails toLocationDetails() {
            return new LocationDetails(latitude, longitude, placeId, address);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class OptionPayload {

        @NotNull
        private OffsetDateTime startAt;

        @NotNull
        private OffsetDateTime endAt;

        private Boolean wholeDay = false;

        TimeRange toRange() {
            return new TimeRange(startAt.toInstant(), endAt.toInstant());
        }

        boolean isWholeDay() {
            return wholeDay != null && wholeDay;
        }
    }
}
