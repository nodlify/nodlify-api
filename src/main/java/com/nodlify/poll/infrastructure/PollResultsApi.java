package com.nodlify.poll.infrastructure;

import com.nodlify.poll.application.PollUseCase;
import com.nodlify.poll.application.VoteUseCase;
import com.nodlify.shared.domain.Identifier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/polls/{pollId}/results")
@RequiredArgsConstructor
@Tag(name = "Public - Poll results", description = "Live poll results projection")
class PollResultsApi {

    private final PollUseCase pollUseCase;
    private final VoteUseCase voteUseCase;

    @GetMapping
    @Operation(summary = "Get live poll results", description = "Status, options, participants (without email) and votes in a single call")
    ResponseEntity<PollResultsResponse> results(@PathVariable String pollId) {
        var poll = pollUseCase.getPoll(Identifier.of(pollId));
        var votes = voteUseCase.getVotesBy(Identifier.of(pollId));
        return ResponseEntity.ok(PollResultsResponse.from(poll, votes));
    }
}
