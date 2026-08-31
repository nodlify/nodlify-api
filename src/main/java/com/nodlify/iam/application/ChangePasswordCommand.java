package com.nodlify.iam.application;


public record ChangePasswordCommand(
        String currentPassword,
        String newPassword
) {
}
