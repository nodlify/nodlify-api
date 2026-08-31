package com.nodlify.iam.infrastructure;

import jakarta.validation.constraints.NotBlank;


record UpdateDisplayNameRequest(
        @NotBlank(message = "displayName must not be blank")
        String displayName
) {
}
