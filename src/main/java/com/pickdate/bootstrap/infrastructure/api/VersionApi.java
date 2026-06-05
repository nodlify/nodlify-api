package com.pickdate.bootstrap.infrastructure.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/version")
@Tag(name = "Public - Application Version", description = "Application version endpoints")
class VersionApi {

    @Value("${pickdate.version}")
    private String version;

    @GetMapping
    ResponseEntity<Version> version() {
        return ResponseEntity.ok().body(new Version(version));
    }

    record Version(String version) {
    }
}
