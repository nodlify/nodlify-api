package com.pickdate.iam.infrastructure;

import com.pickdate.iam.application.ApplicationSetupUseCase;
import com.pickdate.iam.domain.Password;
import com.pickdate.iam.domain.User;
import com.pickdate.shared.domain.DisplayName;
import com.pickdate.shared.domain.Email;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.nonNull;


@RestController
@RequestMapping("/api/v1/iam/setup")
@AllArgsConstructor
@Tag(name = "Admin - Setup", description = "Bootstrap endpoints for initial application configuration. Available only until setup is completed.")
class SetupApi {

    public static final String ADMIN = "Admin";
    private final ApplicationSetupUseCase applicationSetupUseCase;

    @GetMapping
    @Operation(summary = "Get current application setup config")
    ResponseEntity<AppConfigResponse> getConfig() {
        return ResponseEntity.ok(new AppConfigResponse(
                applicationSetupUseCase.getDomainUrl().orElse(null),
                applicationSetupUseCase.setupCompleted()
        ));
    }

    record AppConfigResponse(String domainUrl, boolean setupCompleted) {}

    @PostMapping("/domain")
    @Operation(summary = "Set public domain/origin for the application")
    ResponseEntity<Void> setupDomain(@RequestBody SetupDomainRequest request) {
        applicationSetupUseCase.setupDomain(request.toDomainUrl());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    @Operation(summary = "Create initial admin user")
    ResponseEntity<Void> initializeAdminUser(@RequestBody CreateUserRequest request) {
        var user = new User(
                Email.of(request.email()),
                Password.fromPlaintext(request.password()),
                nonNull(request.displayName())
                        ? DisplayName.of(request.displayName())
                        : DisplayName.of(ADMIN)
        );
        applicationSetupUseCase.setupAdmin(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping
    @Operation(summary = "Complete setup (locks bootstrap endpoints)")
    ResponseEntity<Void> completeSetup() {
        applicationSetupUseCase.completeSetup();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
