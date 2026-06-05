package com.nodlify.shared.functional;

import com.nodlify.shared.domain.Value;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;


public interface Maybe<T> extends Value<T> {

    static <T> Maybe<T> from(T value) {
        if (value == null) return none();
        return new Some<>(value);
    }

    static <T> Maybe<T> none() {
        return None.instance();
    }

    T get();

    default boolean isPresent() {
        return this != none();
    }

    default boolean isEmpty() {
        return !isPresent();
    }

    default <A> A ifPresentGetOrElse(Supplier<A> action, Supplier<A> emptyAction) {
        return isPresent() ? action.get() : emptyAction.get();
    }

    default <A> A ifPresentOrElse(Function<T, A> function, Supplier<A> emptyAction) {
        return isPresent() ? function.apply(getValue()) : emptyAction.get();
    }

    default T orGet(T other) {
        return isPresent() ? getValue() : other;
    }

    default T orNull() {
        return isPresent() ? getValue() : null;
    }

    default Stream<T> stream() {
        if (isEmpty()) {
            return Stream.empty();
        } else {
            return Stream.of(getValue());
        }
    }

    record Some<T>(T value) implements Maybe<T> {

        @Override
        public T get() {
            return value;
        }

        @Override
        public T getValue() {
            return get();
        }
    }

    record None<T>() implements Maybe<T> {

        private static final None<Object> INSTANCE = new None<>();

        @SuppressWarnings("unchecked")
        static <T> None<T> instance() {
            return (None<T>) INSTANCE;
        }

        @Override
        public T get() {
            return null;
        }

        @Override
        public T getValue() {
            return null;
        }
    }
}
