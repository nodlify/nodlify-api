package com.nodlify.iam.infrastructure;

import com.nodlify.iam.application.ChangeDisplayNameCommand;
import com.nodlify.iam.application.ChangePasswordCommand;
import com.nodlify.iam.application.UserData;
import com.nodlify.iam.application.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Public - User", description = "Current user profile and self-service settings")
class CurrentUserApi {

    private static final ResponseEntity<UserData> NO_CONTENT = ResponseEntity.noContent().build();

    private final UserUseCase userUseCase;

    @GetMapping
    @Operation(summary = "Get current user", description = "Returns the authenticated user's profile, or 204 if not authenticated")
    ResponseEntity<UserData> getCurrentUser(Authentication authentication) {
        if (isAnonymous(authentication)) {
            return NO_CONTENT;
        }
        return getUser(authentication)
                .map(ResponseEntity::ok)
                .orElse(NO_CONTENT);
    }

    @PatchMapping("/display-name")
    @Operation(summary = "Change display name", description = "Updates the display name of the authenticated user")
    ResponseEntity<UserData> changeDisplayName(
            Authentication authentication,
            @Valid @RequestBody UpdateDisplayNameRequest request
    ) {
        var updated = userUseCase.changeDisplayName(
                authentication.getName(),
                new ChangeDisplayNameCommand(request.displayName())
        );
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/password")
    @Operation(summary = "Change password", description = "Changes the password of the authenticated user after verifying the current one")
    ResponseEntity<Void> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userUseCase.changePassword(
                authentication.getName(),
                new ChangePasswordCommand(request.currentPassword(), request.newPassword())
        );
        return ResponseEntity.noContent().build();
    }

    private boolean isAnonymous(Authentication authentication) {
        return authentication == null
                || authentication.getPrincipal() == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken;
    }

    private Optional<UserData> getUser(Authentication authentication) {
        try {
            return Optional.ofNullable(userUseCase.getUserByEmail(authentication.getName()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
