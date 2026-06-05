package com.nodlify.poll.infrastructure

import com.nodlify.test.fixture.VoteFixture


trait VoteApiTrait {

    static POLL_ID = '3f5a2721-9ae4-4d71-a9f1-2b1234567890'

    String pollId() {
        POLL_ID
    }

    CastVoteRequest createCastVoteRequest() {
        new CastVoteRequest(
                participantId: VoteFixture.PARTICIPANT_ID,
                optionId: VoteFixture.OPTION_ID,
                availability: "yes"
        )
    }
}
