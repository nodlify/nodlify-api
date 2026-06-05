package com.nodlify.poll.infrastructure;

import com.nodlify.poll.domain.Participant;
import com.nodlify.poll.domain.Phone;
import com.nodlify.shared.domain.DisplayName;
import com.nodlify.shared.domain.Email;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
class RegisterParticipantRequest {

    @NotNull
    private String displayName;

    @Nullable
    private String email;

    @Nullable
    private String phone;

    DisplayName toDisplayName() {
        return DisplayName.of(displayName);
    }

    Email toEmail() {
        return Email.ofNullable(email);
    }

    Phone toPhone() {
        return Phone.ofNullable(phone);
    }

    public Participant toParticipant() {
        return new Participant(toDisplayName())
                .withEmail(toEmail())
                .withPhone(toPhone());
    }
}
