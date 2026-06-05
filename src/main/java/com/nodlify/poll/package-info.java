/**
 * Poll module.
 *
 * <p>
 * This module implements the core polling workflow used to coordinate dates and availability.
 * Users can create a poll, define candidate time ranges and options, invite participants, and
 * collect votes to determine the best matching date/time.
 * </p>
 *
 * <ul>
 *   <li><b>Poll lifecycle</b> – creation, configuration (title/description), and retrieval of polls.</li>
 *   <li><b>Options & time ranges</b> – candidate dates/time slots participants can vote for.</li>
 *   <li><b>Participants</b> – registration of invited participants and their contact details.</li>
 *   <li><b>Voting</b> – casting votes and aggregating availability for decision making.</li>
 * </ul>
 *
 * <p>
 * The module is <b>closed</b>: other modules may interact with Poll only through its public API
 * (use-cases/services, published events, and exposed ports). Internal domain and persistence
 * details are not meant to be referenced directly.
 * </p>
 */

@Nonnull
@ApplicationModule(type = CLOSED)
package com.nodlify.poll;

import jakarta.annotation.Nonnull;
import org.springframework.modulith.ApplicationModule;

import static org.springframework.modulith.ApplicationModule.Type.CLOSED;
