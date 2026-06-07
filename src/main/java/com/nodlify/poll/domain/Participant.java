package com.nodlify.poll.domain;

import com.nodlify.shared.domain.DisplayName;
import com.nodlify.shared.domain.Email;
import com.nodlify.shared.domain.Identifier;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;


@With
@Getter
@Entity
@Table(name = "participants")
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    @With
    @EmbeddedId
    private Identifier id = Identifier.generate();

    @Column(name = "displayName")
    private DisplayName displayName;

    @Column(name = "email")
    private Email email;

    @Column(name = "phone")
    private Phone phone;

    @Column(name = "user_id")
    private Identifier userId;

    public Participant(DisplayName displayName) {
        this.id = Identifier.generate();
        this.displayName = displayName;
    }

    public Participant(DisplayName displayName, Email email, Phone phone) {
        this.displayName = displayName;
        this.email = email;
        this.phone = phone;
    }
}
