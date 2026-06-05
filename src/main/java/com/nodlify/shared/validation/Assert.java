package com.nodlify.shared.validation;

import com.nodlify.shared.domain.Property;
import com.nodlify.shared.exception.IllegalValueException;

import java.time.Instant;
import java.util.regex.Pattern;


public class Assert {

    public static StringAssertion that(String name, String value) {
        return that(new Property<>(name, value));
    }

    public static StringAssertion that(Property<String> property) {
        return new StringAssertion(property);
    }

    public static InstantAssertion that(String name, Instant value) {
        return new InstantAssertion(new Property<>(name, value));
    }

    public static DoubleAssertion that(String name, Double value) {
        return new DoubleAssertion(new Property<>(name, value));
    }

    public static class StringAssertion {

        private final Property<String> property;

        public StringAssertion(Property<String> property) {
            this.property = property;
        }

        public StringAssertion isNotBlank(String message) {
            if (property.value() == null || property.value().isBlank()) {
                throw new IllegalValueException(new Property<>(property.name(), property.value()), message);
            }
            return this;
        }

        public StringAssertion hasMinLength(Integer length, String message) {
            if (property.value().length() < length) {
                throw new IllegalValueException(new Property<>(property.name(), property.value()), message);
            }
            return this;
        }

        public StringAssertion hasMaxLength(Integer length, String message) {
            if (property.value().length() > length) {
                throw new IllegalValueException(new Property<>(property.name(), property.value()), message);
            }
            return this;
        }

        public StringAssertion matches(Pattern pattern, String message) {
            if (!pattern.matcher(property.value()).matches()) {
                throw new IllegalValueException(new Property<>(property.name(), property.value()), message);
            }
            return this;
        }

        public StringAssertion matches(Matcher<String> matcher, String message) {
            if (!matcher.matches(property.value())) {
                throw new IllegalValueException(new Property<>(property.name(), property.value()), message);
            }
            return this;
        }
    }

    public static class InstantAssertion {

        private final Property<Instant> property;

        public InstantAssertion(Property<Instant> property) {
            this.property = property;
        }

        public InstantAssertion isBefore(Instant value, String message) {
            if (property.value().isAfter(value)) {
                throw new IllegalValueException(new Property<>(property.name(), property.value()), message);
            }
            return this;
        }

        public InstantAssertion isAfter(Instant value, String message) {
            if (property.value().isBefore(value)) {
                throw new IllegalValueException(new Property<>(property.name(), property.value()), message);
            }
            return this;
        }

        public InstantAssertion isNotNull(String message) {
            if (property.value() == null) {
                throw new IllegalValueException(new Property<>(property.name(), null), message);
            }
            return this;
        }
    }

    public static class DoubleAssertion {

        private final Property<Double> property;

        public DoubleAssertion(Property<Double> property) {
            this.property = property;
        }

        public DoubleAssertion isNotNull(String message) {
            if (property.value() == null) {
                throw new IllegalValueException(new Property<>(property.name(), null), message);
            }
            return this;
        }

        public DoubleAssertion isFinite(String message) {
            Double value = property.value();
            if (value == null || value.isNaN() || value.isInfinite()) {
                throw new IllegalValueException(new Property<>(property.name(), value), message);
            }
            return this;
        }

        public DoubleAssertion isBetweenInclusive(double min, double max, String message) {
            Double value = property.value();
            if (value == null || value < min || value > max) {
                throw new IllegalValueException(new Property<>(property.name(), value), message);
            }
            return this;
        }
    }
}
