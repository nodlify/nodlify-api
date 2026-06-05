package com.pickdate.iam.infrastructure;

import com.pickdate.iam.application.ApplicationSetupUseCase;
import com.pickdate.shared.web.DefaultView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@DefaultView
@RequiredArgsConstructor
class SetupView {

    private final ApplicationSetupUseCase applicationSetupUseCase;

    @GetMapping("/setup")
    String setupPage() {
        if (applicationSetupUseCase.setupCompleted()) {
            return "redirect:/";
        }
        return "setup";
    }
}
