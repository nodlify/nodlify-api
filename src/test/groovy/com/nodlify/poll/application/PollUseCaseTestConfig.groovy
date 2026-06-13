package com.nodlify.poll.application

import com.nodlify.poll.domain.*
import com.nodlify.shared.domain.DisplayName
import com.nodlify.shared.domain.Email
import com.nodlify.test.stub.PollRepositoryFake

import java.time.Instant

class PollUseCaseTestConfig {

    static PollRepository repository = new PollRepositoryFake()
    static String pollId

    static PollUseCase pollUseCase(PollRepository pollRepository = repository) {
        new PollService(pollRepository)
    }

    static void setupTestData() {
        def poll = new Poll()
                .withTitle(Title.of("Team Sync Poll"))
                .withDescription(Description.of("Please select your preferred time for the weekly team sync."))

        poll.addParticipant(new Participant()
                .withDisplayName(new DisplayName("Alice"))
                .withEmail(new Email("alice@example.com"))
                .withPhone(new Phone("+48-600-123-456"))
        )

        poll.addParticipant(new Participant()
                .withDisplayName(new DisplayName("Bob"))
                .withEmail(new Email("bob@example.com"))
                .withPhone(new Phone("+48-600-654-321"))
        )

        poll.addOption(TimeOption.of(new TimeRange(
                Instant.parse("2025-08-01T08:00:00Z"),
                Instant.parse("2025-08-01T09:00:00Z")
        )))
        poll.addOption(TimeOption.of(new TimeRange(
                Instant.parse("2025-08-02T12:00:00Z"),
                Instant.parse("2025-08-02T13:00:00Z")
        )))
        poll.addOption(TimeOption.of(new TimeRange(
                Instant.parse("2025-08-03T07:00:00Z"),
                Instant.parse("2025-08-03T08:00:00Z")
        )))

        pollId = poll.id.value

        repository.save(poll)
    }
}
