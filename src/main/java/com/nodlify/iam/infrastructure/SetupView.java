package com.nodlify.iam.infrastructure;

import com.nodlify.iam.application.ApplicationSetupUseCase;
import com.nodlify.shared.web.DefaultView;
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
