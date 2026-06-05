package com.nodlify.poll.infrastructure;

import com.nodlify.shared.web.DefaultView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@DefaultView
class VoteView {

    @GetMapping("/vote/{id}")
    String page(@PathVariable String id, Model model) {
        model.addAttribute("pollId", id);
        return "vote";
    }
}
