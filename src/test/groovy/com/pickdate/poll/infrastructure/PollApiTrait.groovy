package com.pickdate.poll.infrastructure

import java.time.Instant
import java.time.OffsetDateTime

trait PollApiTrait {

    private static String NEW_TITLE = "new title"
    private static String NEW_DESCRIPTION = "new description"
    private static Instant VOTING_DEADLINE = Instant.parse("2026-06-15T00:00:00Z")
    private static boolean REQUIRE_PARTICIPANT_NAMES = true

    String title() {
        NEW_TITLE
    }

    String description() {
        NEW_DESCRIPTION
    }

    Instant votingDeadline() {
        VOTING_DEADLINE
    }

    boolean requireParticipantNames() {
        REQUIRE_PARTICIPANT_NAMES
    }

    CreateOptionRequest createOptionRequest() {
        new CreateOptionRequest(
                startAt: OffsetDateTime.parse("2995-08-01T10:00:00Z"),
                endAt: OffsetDateTime.parse("2995-08-01T11:00:00Z"),
                wholeDay: true
        )
    }

    RegisterParticipantRequest registerParticipantRequest() {
        new RegisterParticipantRequest(
                displayName: "john",
                email: "john@wick.com"
        )
    }

    CreatePollRequest createPollRequest() {
        new CreatePollRequest(
                title: NEW_TITLE,
                description: NEW_DESCRIPTION,
                votingDeadline: VOTING_DEADLINE,
                requireParticipantNames: REQUIRE_PARTICIPANT_NAMES
        )
    }
}
