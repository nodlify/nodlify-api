package com.nodlify.iam.infrastructure;

import com.nodlify.iam.application.UserUseCase;
import com.nodlify.iam.domain.Password;
import com.nodlify.iam.domain.User;
import com.nodlify.shared.domain.DisplayName;
import com.nodlify.shared.domain.Email;
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
