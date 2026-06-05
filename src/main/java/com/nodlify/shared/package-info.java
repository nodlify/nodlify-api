/**
 * Shared module.
 *
 * <p>
 * This module provides shared building blocks used across multiple application modules.
 * It contains small, stable and reusable abstractions that are not owned by a single feature module.
 * </p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li><b>Core domain primitives</b> – common value objects and identifiers (e.g. {@code Identifier}, {@code Email},
 *       {@code DisplayName}) and base abstractions (e.g. {@code Value}).</li>
 *   <li><b>Validation utilities</b> – lightweight assertion and matcher helpers used to enforce invariants.</li>
 *   <li><b>Error model</b> – a structured problem representation and exception types used consistently across modules.</li>
 *   <li><b>Web context</b> – request-scoped technical details (e.g. {@code RequestDetails}) used by security/auditing.</li>
 * </ul>
 *
 * <h2>Non-responsibilities</h2>
 * <ul>
 *   <li>No feature-specific business rules.</li>
 *   <li>No broad "utility dump" code; shared abstractions should be stable and intentionally reused.</li>
 * </ul>
 *
 * <p>
 * The module is <b>open</b>: it acts as a shared kernel and may be referenced by other modules.
 * Keep its public surface small, stable and dependency-light.
 * </p>
 */

@Nonnull
@ApplicationModule(type = OPEN)
package com.nodlify.shared;

import jakarta.annotation.Nonnull;
import org.springframework.modulith.ApplicationModule;

import static org.springframework.modulith.ApplicationModule.Type.OPEN;
