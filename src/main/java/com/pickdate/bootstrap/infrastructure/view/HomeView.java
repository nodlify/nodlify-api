package com.pickdate.bootstrap.infrastructure.view;

import com.pickdate.shared.web.DefaultView;
import org.springframework.web.bind.annotation.GetMapping;


@DefaultView
class HomeView {

    @GetMapping("/")
    String landing() {
        return "index";
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
