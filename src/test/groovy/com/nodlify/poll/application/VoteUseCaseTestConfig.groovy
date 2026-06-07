package com.nodlify.poll.application

import com.nodlify.poll.domain.Availability
import com.nodlify.poll.domain.PollRepository
import com.nodlify.poll.domain.Vote
import com.nodlify.poll.domain.VoteRepository
import com.nodlify.shared.domain.Identifier
import com.nodlify.test.stub.PollRepositoryFake
import com.nodlify.test.stub.VoteRepositoryFake

import static com.nodlify.poll.domain.Vote.VoteId

class VoteUseCaseTestConfig {

    static VoteRepository repositoryFake = new VoteRepositoryFake()
    static PollRepository pollRepositoryFake = new PollRepositoryFake()

    static VoteUseCase voteUseCase(VoteRepository voteRepository = repositoryFake,
                                   PollRepository pollRepository = pollRepositoryFake) {
        new VoteService(voteRepository, pollRepository)
    }

    static void setupFakeData() {
        Identifier pollId = Identifier.of('3f5a2721-9ae4-4d71-a9f1-2b1234567890')
        Identifier alice = Identifier.of('a1b2c3d4-0000-1111-2222-333344445555')
        Identifier bob = Identifier.of('b2c3d4e5-1111-2222-3333-444455556666')
        Identifier opt1 = Identifier.of('d4e5f6a7-2222-3333-4444-555566667777')
        Identifier opt2 = Identifier.of('e5f6a7b8-3333-4444-5555-666677778888')

        repositoryFake.save(
                new Vote()
                        .with(new VoteId(alice, opt1))
                        .with(pollId)
                        .with(Availability.YES)
        )

        repositoryFake.save(
                new Vote()
                        .with(new VoteId(bob, opt2))
                        .with(pollId)
                        .with(Availability.MAYBE)
        )
    }
}
