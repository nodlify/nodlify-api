package com.nodlify.iam.infrastructure;

record CreateUserRequest(
        String email,
        String password,
        String displayName
) {
}
