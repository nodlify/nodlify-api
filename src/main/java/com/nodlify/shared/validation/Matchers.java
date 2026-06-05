package com.nodlify.shared.validation;

import java.util.Arrays;
import java.util.regex.Pattern;


public final class Matchers {

    private Matchers() {
    }

    public static Matcher<String> oneOf(Enum<?>... values) {
        String[] array = Arrays.stream(values).map(Enum::name).toArray(String[]::new);
        return new OneOfMatcher(array);
    }

    public static Matcher<String> oneOf(String... values) {
        return new OneOfMatcher(values);
    }

    public static Matcher<String> isValidPort() {
        return new PortMatcher();
    }

    public static Matcher<String> oneRegexOf(Pattern... regexes) {
        return new RegexMatcher(regexes);
    }
}

final class OneOfMatcher implements Matcher<String> {

    private final String[] values;

    public OneOfMatcher(String[] values) {
        this.values = values;
    }

    @Override
    public boolean matches(String value) {
        return Arrays.asList(values).contains(value);
    }
}

final class PortMatcher implements Matcher<String> {

    private static final int MIN_PORT = 1;
    private static final int MAX_PORT = 65535;

    @Override
    public boolean matches(String value) {
        if (value == null) return false;
        int portIndex = value.lastIndexOf(':');
        if (portIndex == -1) return false;
        String portStr = value.substring(portIndex + 1);
        if (portStr.isBlank()) return false;
        if (!portStr.chars().allMatch(Character::isDigit)) return false;
        try {
            int port = Integer.parseInt(portStr);
            return port >= MIN_PORT && port <= MAX_PORT;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

final class RegexMatcher implements Matcher<String> {

    private final Pattern[] regexes;

    public RegexMatcher(Pattern[] regexes) {
        this.regexes = regexes;
    }

    @Override
    public boolean matches(String value) {
        return Arrays.stream(regexes)
                .anyMatch(regex -> regex.matcher(value).matches());
    }
}
