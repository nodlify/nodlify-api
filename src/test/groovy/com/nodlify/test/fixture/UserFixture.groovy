package com.nodlify.test.fixture

import com.nodlify.iam.domain.Authority
import com.nodlify.iam.domain.Password
import com.nodlify.iam.domain.User
import com.nodlify.shared.domain.DisplayName
import com.nodlify.shared.domain.Email

class UserFixture {

    static final String JOHN_EMAIL = "john@example.com"
    static final String JOHN_DISPLAY_NAME = "John Doe"

    static User SOME_ADMIN = new User()
            .withEmail(Email.of("admin@email.com"))
            .withPassword(Password.fromPlaintext("superSecretPass!"))
            .addAuthority(Authority.ADMIN)

    static User SOME_USER = new User()
            .withEmail(Email.of("user@email.com"))
            .withPassword(Password.fromPlaintext("superSecretPass!"))
            .addAuthority(Authority.USER)

    static User john() {
        new User(
                Email.of(JOHN_EMAIL),
                Password.fromPlaintext("Password1"),
                DisplayName.of(JOHN_DISPLAY_NAME)
        ).addAuthority(Authority.USER)
    }
}
