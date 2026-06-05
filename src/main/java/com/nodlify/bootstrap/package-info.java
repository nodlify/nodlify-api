/**
 * Bootstrap module.
 *
 * <p>
 * This module contains application bootstrap and configuration code required to assemble and start
 * the application. It is responsible for technical wiring and runtime configuration, not for shared
 * domain primitives or feature-specific business logic.
 * </p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li><b>Application startup</b> - main application entry point and bootstrapping configuration.</li>
 *   <li><b>Framework configuration</b> - Spring, web, persistence, security, OpenAPI/Swagger and other infrastructure setup.</li>
 *   <li><b>Bean wiring</b> - composition of module services, adapters and infrastructure implementations.</li>
 *   <li><b>Runtime configuration</b> - application properties, environment-specific settings and technical integration setup.</li>
 * </ul>
 *
 * <h2>Non-responsibilities</h2>
 * <ul>
 *   <li>No feature-specific business logic.</li>
 *   <li>No shared domain primitives, validation helpers or reusable value objects - those belong to {@code shared}.</li>
 *   <li>No module-owned persistence or API logic unless it is strictly required for application configuration.</li>
 * </ul>
 *
 * <p>
 * The module is <b>closed</b>: other modules should not depend on bootstrap internals. Dependencies should point
 * from bootstrap to feature/shared modules, not the other way around.
 * </p>
 */

@Nonnull
@ApplicationModule(type = CLOSED)
package com.nodlify.bootstrap;

import jakarta.annotation.Nonnull;
import org.springframework.modulith.ApplicationModule;

import static org.springframework.modulith.ApplicationModule.Type.CLOSED;
