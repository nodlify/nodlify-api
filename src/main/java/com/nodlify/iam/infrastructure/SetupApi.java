package com.nodlify.iam.infrastructure;

import com.nodlify.iam.application.ApplicationSetupUseCase;
import com.nodlify.iam.application.CreateUserCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.nonNull;


@RestController
@RequestMapping("/api/v1/setup")
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

    @PostMapping("/domain")
    @Operation(summary = "Set public domain/origin for the application")
    ResponseEntity<Void> setupDomain(@RequestBody SetupDomainRequest request) {
        applicationSetupUseCase.setupDomain(request.toDomainUrl());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    @Operation(summary = "Create initial admin user")
    ResponseEntity<Void> initializeAdminUser(@RequestBody CreateUserRequest request) {
        var displayName = nonNull(request.displayName()) ? request.displayName() : ADMIN;
        applicationSetupUseCase.setupAdmin(
                new CreateUserCommand(request.email(), request.password(), displayName)
        );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping
    @Operation(summary = "Complete setup (locks bootstrap endpoints)")
    ResponseEntity<Void> completeSetup() {
        applicationSetupUseCase.completeSetup();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    record AppConfigResponse(String domainUrl, boolean setupCompleted) {}
}
