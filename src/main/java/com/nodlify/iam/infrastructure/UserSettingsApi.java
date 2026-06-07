package com.nodlify.iam.infrastructure;

import com.nodlify.iam.application.ChangeDisplayNameCommand;
import com.nodlify.iam.application.ChangePasswordCommand;
import com.nodlify.iam.application.UserData;
import com.nodlify.iam.application.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
@Tag(name = "Public - User settings", description = "Self-service settings for the authenticated user")
class UserSettingsApi {

    private final UserUseCase userUseCase;

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
}
