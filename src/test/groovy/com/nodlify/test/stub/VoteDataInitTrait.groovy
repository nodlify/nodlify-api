package com.nodlify.test.stub

import com.nodlify.poll.domain.*
import com.nodlify.shared.domain.DisplayName
import com.nodlify.shared.domain.Email
import org.springframework.beans.factory.annotation.Autowired

import java.time.Instant

import static com.nodlify.test.fixture.VoteFixture.*

trait VoteDataInitTrait {

    def title = Title.of('Team Sync Poll')
    def desc = Description.of('Please select your preferred time for the weekly team sync.')

    Poll poll = Poll.from(title, desc)
            .withId(POLL_ID)

    Participant alice = new Participant()
            .withDisplayName(DisplayName.of('Alice'))
            .withEmail(Email.of('alice@example.com'))
            .withPhone(Phone.of('+48-600-123-456'))
            .withId(PARTICIPANT_IDENTIFIER)

    TimeRange timeRange1 = new TimeRange(
            Instant.parse('2025-08-01T10:00:00+02:00'),
            Instant.parse('2025-08-01T11:00:00+02:00')
    )

    TimeRange timeRange2 = new TimeRange(
            Instant.parse('2025-08-02T14:00:00+02:00'),
            Instant.parse('2025-08-02T15:00:00+02:00')
    )

    Option option1 = Option.from(timeRange1)
            .withId(OPTION_IDENTIFIER)

    Option option2 = Option.from(timeRange2)

    @Autowired
    PollRepository pollRepo

    void initData() {
        poll.addParticipant(alice)
        poll.addOption(option1)
        poll.addOption(option2)
        pollRepo.save(poll)
    }

    void clear() {
        pollRepo.deleteAll()
    }
}
