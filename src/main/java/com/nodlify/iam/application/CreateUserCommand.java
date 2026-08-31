package com.nodlify.iam.application;


public record CreateUserCommand(
        String email,
        String password,
        String displayName
) {
}
