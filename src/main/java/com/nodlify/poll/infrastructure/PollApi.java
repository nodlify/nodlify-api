package com.nodlify.poll.infrastructure;

import com.nodlify.iam.application.UserUseCase;
import com.nodlify.poll.application.CreatePollCommand;
import com.nodlify.poll.application.ParticipantData;
import com.nodlify.poll.application.PollData;
import com.nodlify.poll.application.PollUseCase;
import com.nodlify.poll.domain.Participant;
import com.nodlify.shared.domain.DisplayName;
import com.nodlify.shared.domain.Email;
import com.nodlify.shared.domain.Identifier;
import com.nodlify.shared.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
    private final UserUseCase userUseCase;

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
        var response = PollResponse.from(data, organizerName(data));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new poll", description = "Creates a new poll with provided title, description, settings, optional location, and date options")
    ResponseEntity<PollData> createPoll(@Valid @RequestBody CreatePollRequest req) {
        var command = new CreatePollCommand(
                req.getTitle(),
                req.getDescription(),
                req.toLocationDetails(),
                req.toOptions(),
                req.getVotingDeadline(),
                req.isAllowAnonymous(),
                req.getPollType(),
                req.getChoiceType()
        );
        return ResponseEntity.status(CREATED).body(pollUseCase.createPoll(command));
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
        var data = pollUseCase.addOption(Identifier.of(pollId), request.toOption());
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
    ResponseEntity<ParticipantData> registerParticipant(
            @PathVariable String pollId,
            @RequestBody(required = false) RegisterParticipantRequest request,
            Authentication authentication
    ) {
        var participant = participantFrom(request, authentication);
        var registered = pollUseCase.registerParticipant(Identifier.of(pollId), participant);
        return ResponseEntity.status(CREATED).body(registered);
    }

    @GetMapping("/{pollId}/participants/me")
    @Operation(summary = "Get my participant", description = "Returns the authenticated user's participant for the poll, or 204 if not registered")
    ResponseEntity<ParticipantData> myParticipant(
            @PathVariable String pollId,
            Authentication authentication
    ) {
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.noContent().build();
        }
        var user = userUseCase.getUserByEmail(authentication.getName());
        return pollUseCase.findParticipant(Identifier.of(pollId), Identifier.of(user.id()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
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

    @PatchMapping("/{pollId}/status")
    @Operation(summary = "Change poll status", description = "Marks the poll as DECIDED or CLOSED, or reopens it (VOTING)")
    ResponseEntity<PollData> changeStatus(
            @PathVariable String pollId,
            @RequestBody ChangeStatusRequest request
    ) {
        var data = pollUseCase.changeStatus(Identifier.of(pollId), request.toStatus());
        return ResponseEntity.ok(data);
    }

    private Participant participantFrom(RegisterParticipantRequest request, Authentication authentication) {
        if (isAuthenticated(authentication)) {
            var user = userUseCase.getUserByEmail(authentication.getName());
            return new Participant(DisplayName.of(user.displayName()))
                    .withEmail(Email.of(user.email()))
                    .withUserId(Identifier.of(user.id()));
        }
        if (request == null) {
            throw new IllegalArgumentException("Participant details are required");
        }
        return request.toParticipant();
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private String organizerName(PollData poll) {
        var email = poll.organizer();
        if (email == null || email.isBlank()) {
            return email;
        }
        try {
            var displayName = userUseCase.getUserByEmail(email).displayName();
            return displayName == null || displayName.isBlank() ? email : displayName;
        } catch (NotFoundException ignored) {
            return email;
        }
    }
}
