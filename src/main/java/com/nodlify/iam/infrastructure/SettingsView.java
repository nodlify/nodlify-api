package com.nodlify.iam.infrastructure;

import com.nodlify.shared.web.DefaultView;
import org.springframework.web.bind.annotation.GetMapping;


@DefaultView
class SettingsView {

    @GetMapping("/settings")
    String page() {
        return "settings";
    }
}
