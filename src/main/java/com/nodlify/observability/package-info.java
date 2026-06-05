/**
 * Observability module.
 *
 * <p>
 * This module is responsible for operational visibility and diagnostics of the application.
 * It captures and persists operational signals such as:
 * </p>
 *
 * <ul>
 *   <li><b>Problem logs</b> – structured representations of HTTP/API failures (e.g. 4xx/5xx) including
 *       status, title/detail, instance, stack traces and validation errors.</li>
 *   <li><b>Audit logs</b> – security- and user-related actions (e.g. authentication successes/failures)
 *       with optional payloads for additional context.</li>
 * </ul>
 *
 * <p>
 * The module is <b>closed</b>: other modules may interact with it only through its public API
 * (published events, exposed services or ports). Domain internals are not meant to be referenced directly.
 * </p>
 */

@Nonnull
@ApplicationModule(type = CLOSED)
package com.nodlify.observability;

import jakarta.annotation.Nonnull;
import org.springframework.modulith.ApplicationModule;

import static org.springframework.modulith.ApplicationModule.Type.CLOSED;
