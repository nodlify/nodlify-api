package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    private Boolean allowAnonymous;

    private String type;

    private String choiceType;

    @Valid
    private LocationPayload location;

    @Valid
    private List<OptionPayload> options;

    boolean isAllowAnonymous() {
        return Boolean.TRUE.equals(allowAnonymous);
    }

    Title getTitle() {
        return Title.of(title);
    }

    Description getDescription() {
        return Description.ofNullable(description);
    }

    PollType getPollType() {
        return type == null ? PollType.TIME : PollType.valueOf(type.strip().toUpperCase());
    }

    ChoiceType getChoiceType() {
        return choiceType == null ? ChoiceType.MULTIPLE : ChoiceType.valueOf(choiceType.strip().toUpperCase());
    }

    LocationDetails toLocationDetails() {
        return location == null ? null : location.toLocationDetails();
    }

    List<Option> toOptions() {
        var pollType = getPollType();
        return getOptions().stream()
                .map(option -> option.toOption(pollType))
                .toList();
    }

    private List<OptionPayload> getOptions() {
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

        private OffsetDateTime startAt;
        private OffsetDateTime endAt;
        private Boolean wholeDay = false;
        private String label;

        Option toOption(PollType pollType) {
            if (pollType == PollType.SIMPLE) {
                return TextOption.of(label);
            }
            var range = new TimeRange(startAt.toInstant(), endAt.toInstant());
            return TimeOption.of(range, Boolean.TRUE.equals(wholeDay));
        }
    }
}
