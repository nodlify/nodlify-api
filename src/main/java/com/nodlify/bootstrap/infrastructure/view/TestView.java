package com.nodlify.bootstrap.infrastructure.view;

import com.nodlify.shared.web.DefaultView;
import org.springframework.web.bind.annotation.GetMapping;


@DefaultView
class TestView {

    @GetMapping("/test")
    String landing() {
        return "pages/test";
    }
}
