package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.Description;
import com.nodlify.poll.domain.Title;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
class UpdatePollRequest {

    @Size.List({
            @Size(min = 3, message = "{validation.title.size.too_short}"),
            @Size(max = 255, message = "{validation.title.size.too_long}")
    })
    @NotBlank(message = "{validation.title.not_blank}")
    private String title;

    private String description;

    Title getTitle() {
        return Title.of(title);
    }

    Description getDescription() {
        return Description.ofNullable(description);
    }
}
