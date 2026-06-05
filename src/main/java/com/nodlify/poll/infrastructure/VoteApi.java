package com.nodlify.poll.infrastructure;

import com.nodlify.poll.application.VoteData;
import com.nodlify.poll.application.VoteUseCase;
import com.nodlify.shared.domain.Identifier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequestMapping("/api/v1/polls/{pollId}/votes")
@RequiredArgsConstructor
@Tag(name = "Public - Votes", description = "Poll voting endpoints")
class VoteApi {

    private final VoteUseCase voteUseCase;

    @GetMapping
    @Operation(summary = "List votes", description = "Lists all votes for a given poll")
    ResponseEntity<List<VoteData>> getVotes(@PathVariable String pollId) {
        List<VoteData> votes = voteUseCase.getVotesBy(Identifier.of(pollId));
        return ResponseEntity.ok(votes);
    }

    @PostMapping
    @Operation(summary = "Cast a vote", description = "Casts a vote for a given poll option")
    ResponseEntity<Void> castVote(
            @PathVariable String pollId,
            @RequestBody CastVoteRequest request
    ) {
        voteUseCase.castVote(request.toCommand(pollId));
        return ResponseEntity.status(CREATED).build();
    }
}
