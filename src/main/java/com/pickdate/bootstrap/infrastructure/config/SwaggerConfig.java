package com.pickdate.bootstrap.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
class SwaggerConfig {

    private static final String API_PATH_PATTERN = "/api/v1/**";

    @Bean
    public OpenAPI swaggerDocumentation() {
        var components = new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        var globalSecurity = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(components)
                .addSecurityItem(globalSecurity)
                .info(new Info().title("Pickdate API")
                        .description("Public REST API for managing polls, votes, user accounts, activity events and problem/error reports.")
                        .version("v1")
                        .license(new License().name("AGPL-3.0").url("https://www.gnu.org/licenses/agpl-3.0.en.html"))
                );
    }

    @Bean
    GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("api")
                .displayName("Pickdate API")
                .pathsToMatch(API_PATH_PATTERN)
                .build();
    }
}
