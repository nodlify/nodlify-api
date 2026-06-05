package com.nodlify.observability.problem.infrastructure;

import com.nodlify.shared.web.DefaultView;
import org.springframework.web.bind.annotation.GetMapping;


@DefaultView
class ProblemLogView {

    @GetMapping("/observability/problems")
    String problemsPage() {
        return "problems";
    }
}
