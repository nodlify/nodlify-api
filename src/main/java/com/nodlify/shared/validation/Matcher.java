package com.nodlify.shared.validation;


public interface Matcher<T> {

    boolean matches(T value);
}

