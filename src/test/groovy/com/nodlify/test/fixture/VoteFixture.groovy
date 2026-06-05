package com.nodlify.test.fixture

import com.nodlify.poll.application.VoteData
import com.nodlify.shared.domain.Identifier

class VoteFixture {
    public static final String PARTICIPANT_ID = 'a1b2c3d4-0000-1111-2222-333344445555'
    public static final String OPTION_ID = 'd4e5f6a7-2222-3333-4444-555566667777'

    public static final Identifier PARTICIPANT_IDENTIFIER = Identifier.of(PARTICIPANT_ID)
    public static final Identifier OPTION_IDENTIFIER = Identifier.of(OPTION_ID)
    public static final Identifier POLL_ID = Identifier.of('3f5a2721-9ae4-4d71-a9f1-2b1234567890')

    static List<VoteData> someVoteData() {
        [
                new VoteData(
                        POLL_ID.value,
                        PARTICIPANT_ID,
                        OPTION_ID,
                        "YES"
                )
        ]
    }

    static String castVoteRequestJson() {
        """
            {
                "participantId": "123",
                "optionId": "321",
                "availability": "MAYBE"
            }
        """
    }
}
