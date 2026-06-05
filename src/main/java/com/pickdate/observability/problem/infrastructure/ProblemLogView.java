package com.pickdate.observability.problem.infrastructure;

import com.pickdate.shared.web.DefaultView;
import org.springframework.web.bind.annotation.GetMapping;


@DefaultView
class ProblemLogView {

    @GetMapping("/observability/problems")
    String problemsPage() {
        return "problems";
    }
}
