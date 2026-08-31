package com.nodlify.iam.application;

import com.nodlify.iam.domain.User;

import java.util.List;

import static com.nodlify.shared.domain.Value.valueOrNull;


public record UserData(
        String id,
        String email,
        String displayName,
        List<String> roles
) {

    public static UserData from(User user) {
        return new UserData(
                valueOrNull(user.getId()),
                valueOrNull(user.getEmail()),
                valueOrNull(user.getDisplayName()),
                user.getAuthoritiesAsString()
        );
    }
}
