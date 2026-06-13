package com.nodlify.bootstrap.infrastructure.config;

import gg.jte.Content;

import java.time.Year;


public class JteContext {
    private static final ThreadLocal<Localizer> context = new ThreadLocal<>();

    public static Content localize(String key) {
        return context.get().localize(key);
    }

    public static String message(String key) {
        return context.get().lookup(key);
    }

    public static String message(String key, Object... parameters) {
        return context.get().lookup(key, parameters);
    }

    public static Content localize(String key, Object... params) {
        return context.get().localize(key, params);
    }

    static void init(Localizer localizer) {
        context.set(localizer);
    }

    static void dispose() {
        context.remove();
    }

    public static String currentYear() {
        return  String.valueOf(Year.now().getValue());
    }

    public static String language() {
        return context.get().language();
    }
}
