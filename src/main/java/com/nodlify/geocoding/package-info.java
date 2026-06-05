/**
 * Geocoding module.
 *
 * <p>
 * Thin server-side proxy in front of an external geocoding provider (Nominatim / OpenStreetMap).
 * It exists so the browser never calls the provider directly: this lets us attach a proper
 * identifying {@code User-Agent} (required by the Nominatim usage policy), centralise rate
 * limiting, and avoid exposing third-party endpoints or CORS concerns to the frontend.
 * </p>
 *
 * <ul>
 *   <li><b>Forward search</b> – free-text query to a list of candidate places.</li>
 *   <li><b>Reverse geocoding</b> – latitude/longitude to a single human-readable address.</li>
 * </ul>
 *
 * <p>
 * The module exposes only a web API and depends on no other module's internals. Responses are
 * normalised to a provider-agnostic shape so callers are not coupled to Nominatim's payload.
 * </p>
 */

@Nonnull
@ApplicationModule
package com.nodlify.geocoding;

import jakarta.annotation.Nonnull;
import org.springframework.modulith.ApplicationModule;
