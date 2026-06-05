/**
 * IAM (Identity & Access Management) module.
 *
 * <p>
 * This module owns authentication and authorization concerns for the application.
 * It provides the core user model and access control primitives, including:
 * </p>
 *
 * <ul>
 *   <li><b>User management</b> – user identities, credentials (password handling), and persistence.</li>
 *   <li><b>Authorities</b> – role/authority assignments and enforcement of access policies.</li>
 *   <li><b>Authentication flows</b> – login, remember-me support, and integration with Spring Security.</li>
 *   <li><b>Application setup</b> – initial bootstrap/setup state and configuration required to enable the system.</li>
 * </ul>
 *
 * <p>
 * The module is <b>closed</b>: other modules may interact with IAM only through its public API
 * (use-cases/services, published events, and exposed ports). Direct access to internal persistence
 * and infrastructure types is not intended.
 * </p>
 */

@Nonnull
@ApplicationModule(type = CLOSED)
package com.nodlify.iam;

import jakarta.annotation.Nonnull;
import org.springframework.modulith.ApplicationModule;

import static org.springframework.modulith.ApplicationModule.Type.CLOSED;
