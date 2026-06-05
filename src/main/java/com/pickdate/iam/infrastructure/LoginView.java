package com.pickdate.iam.infrastructure;

import com.pickdate.iam.application.UserUseCase;
import com.pickdate.iam.domain.Password;
import com.pickdate.iam.domain.User;
import com.pickdate.shared.domain.DisplayName;
import com.pickdate.shared.domain.Email;
import com.pickdate.shared.web.DefaultView;
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
        return "register";
    }

    @PostMapping("/register")
    String register(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String displayName
    ) {
        var user = new User(
                Email.of(email),
                Password.fromPlaintext(password),
                DisplayName.of(displayName)
        );
        userUseCase.createUser(user);

        return "redirect:/login";
    }

    @GetMapping("/reset-password")
    String resetPasswordPage() {
        return "reset-password";
    }
}
