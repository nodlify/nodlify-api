package com.nodlify.poll.infrastructure;

import com.nodlify.poll.application.PollData;
import com.nodlify.poll.application.PollUseCase;
import com.nodlify.poll.domain.Option;
import com.nodlify.shared.domain.Identifier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/v1/polls")
@RequiredArgsConstructor
@Tag(name = "Public - Polls", description = "Poll and option endpoints")
class PollApi {

    private final PollUseCase pollUseCase;

    @GetMapping
    @Operation(summary = "List my polls", description = "Returns all polls created by the authenticated user")
    ResponseEntity<List<PollData>> getMyPolls(Authentication authentication) {
        var polls = pollUseCase.getMyPolls(authentication.getName());
        return ResponseEntity.ok(polls);
    }

    @GetMapping("/{pollId}")
    @Operation(summary = "Get poll by id", description = "Returns poll details for given identifier")
    ResponseEntity<PollResponse> getPoll(@PathVariable String pollId) {
        var data = pollUseCase.getPoll(Identifier.of(pollId));
        var response = PollResponse.from(data);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new poll", description = "Creates a new poll with provided title, description, settings, optional location, and date options")
    ResponseEntity<PollData> createPoll(@Valid @RequestBody CreatePollRequest req) {
        var options = req.getOptions().stream()
                .map(option -> Option.from(option.toRange(), option.isWholeDay()))
                .toList();
        var data = pollUseCase.createPoll(
                req.getTitle(),
                req.getDescription(),
                req.toLocationDetails(),
                options,
                req.getVotingDeadline(),
                req.isRequireParticipantNames()
        );
        return ResponseEntity.status(CREATED).body(data);
    }

    @PatchMapping("/{pollId}")
    @Operation(summary = "Update poll details", description = "Updates the title and description of an existing poll")
    ResponseEntity<PollData> updatePoll(
            @PathVariable String pollId,
            @Valid @RequestBody UpdatePollRequest request
    ) {
        var data = pollUseCase.updateDetails(Identifier.of(pollId), request.getTitle(), request.getDescription());
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{pollId}")
    @Operation(summary = "Delete poll", description = "Deletes poll by given identifier")
    ResponseEntity<?> deletePoll(@PathVariable String pollId) {
        pollUseCase.deletePoll(Identifier.of(pollId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{pollId}/options")
    @Operation(summary = "Add option to poll", description = "Adds an option (time range or whole-day) to an existing poll")
    ResponseEntity<PollData> addOption(
            @PathVariable String pollId,
            @Valid @RequestBody CreateOptionRequest request
    ) {
        var option = Option.from(request.toRange(), request.isWholeDay());
        var data = pollUseCase.addOption(Identifier.of(pollId), option);
        return ResponseEntity.status(CREATED).body(data);
    }

    @DeleteMapping("/{pollId}/options/{optionId}")
    @Operation(summary = "Remove option from poll", description = "Removes an option from an existing poll")
    ResponseEntity<PollData> removeOption(
            @PathVariable String pollId,
            @PathVariable String optionId
    ) {
        var data = pollUseCase.removeOption(Identifier.of(pollId), Identifier.of(optionId));
        return ResponseEntity.status(OK).body(data);
    }

    @PostMapping("/{pollId}/participants")
    @Operation(summary = "Register participant", description = "Registers a participant in the poll with optional email")
    ResponseEntity<PollData> registerParticipant(
            @PathVariable String pollId,
            @RequestBody RegisterParticipantRequest request
    ) {
        var data = pollUseCase.registerParticipant(Identifier.of(pollId), request.toParticipant());
        return ResponseEntity.status(CREATED).body(data);
    }

    @PostMapping("/{pollId}/location")
    @Operation(summary = "Add location to poll", description = "Adds a location to an existing poll")
    ResponseEntity<PollData> addLocation(
            @PathVariable String pollId,
            @RequestBody CreateLocationRequest request
    ) {
        var data = pollUseCase.addLocation(Identifier.of(pollId), request.toLocationData());
        return ResponseEntity.status(CREATED).body(data);
    }
}
