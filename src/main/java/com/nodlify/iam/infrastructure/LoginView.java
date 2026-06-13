package com.nodlify.iam.infrastructure;

import com.nodlify.iam.application.CreateUserCommand;
import com.nodlify.iam.application.UserUseCase;
import com.nodlify.shared.web.DefaultView;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@DefaultView
@RequiredArgsConstructor
class LoginView {

    private final UserUseCase userUseCase;

    @GetMapping("/login")
    String loginPage(
            @RequestParam(value = "error", required = false) String error,
            Model model
    ) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }

    @GetMapping("/register")
    String registerPage() {
        return "pages/register";
    }

    @PostMapping("/register")
    String register(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String displayName
    ) {
        userUseCase.createUser(new CreateUserCommand(email, password, displayName));

        return "redirect:/login";
    }

    @GetMapping("/reset-password")
    String resetPasswordPage() {
        return "reset-password";
    }
}
