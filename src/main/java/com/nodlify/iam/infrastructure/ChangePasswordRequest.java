package com.nodlify.iam.infrastructure;

import jakarta.validation.constraints.NotBlank;


record ChangePasswordRequest(
        @NotBlank(message = "currentPassword must not be blank")
        String currentPassword,

        @NotBlank(message = "newPassword must not be blank")
        String newPassword
) {
}
