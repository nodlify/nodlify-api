package com.nodlify.iam.domain;

import com.nodlify.shared.domain.Property;
import com.nodlify.shared.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    static final String MESSAGE = "User not found";

    public UserNotFoundException(Property<Object> property, String message) {
        super(property, message);
    }

    public static UserNotFoundException withEmail(String email) {
        return new UserNotFoundException(Property.of("email", email), MESSAGE);
    }

    public static RuntimeException withId(String id) {
        return new UserNotFoundException(Property.of("id", id), MESSAGE);
    }
}
