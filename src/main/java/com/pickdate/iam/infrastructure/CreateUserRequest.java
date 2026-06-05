package com.pickdate.iam.infrastructure;

record CreateUserRequest(
        String email,
        String password,
        String displayName
) {
}
