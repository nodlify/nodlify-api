package com.nodlify.bootstrap.infrastructure.config;

import gg.jte.Content;
import gg.jte.support.LocalizationSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;


@RequiredArgsConstructor
public final class Localizer implements LocalizationSupport {

    private final MessageSource messageSource;
    private final Locale locale;

    @Override
    public String lookup(String key) {
        return messageSource.getMessage(key, null, key, locale);
    }

    public String lookup(String key, Object[] parameters) {
        return messageSource.getMessage(key, parameters, key, locale);
    }

    public String language() {
        return locale.getLanguage();
    }

    public Content localize(String key) {
        return LocalizationSupport.super.localize(key);
    }

    public Content localize(String key, Object... parameters) {
        return LocalizationSupport.super.localize(key, parameters);
    }
}
