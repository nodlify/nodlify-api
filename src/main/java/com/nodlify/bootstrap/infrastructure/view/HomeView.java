package com.nodlify.bootstrap.infrastructure.view;

import com.nodlify.shared.web.DefaultView;
import org.springframework.web.bind.annotation.GetMapping;


@DefaultView
class HomeView {

    @GetMapping("/")
    String landing() {
        return "pages/index";
    }

    @GetMapping("/home")
    String home() {
        return "home";
    }

    @GetMapping("/create-poll")
    String createPoll() {
        return "create-poll";
    }
}
