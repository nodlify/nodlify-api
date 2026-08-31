package com.nodlify.bootstrap.infrastructure.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;


@RestController
@Tag(name = "Public - Messages", description = "Localized UI messages for the frontend")
class MessagesApi {

    @GetMapping("/i18n/{lang}.json")
    Map<String, String> messages(@PathVariable String lang) {
        var control = ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);
        var bundle = ResourceBundle.getBundle("messages", Locale.forLanguageTag(lang), control);
        return bundle.keySet().stream()
                .collect(toMap(identity(), bundle::getString));
    }
}
