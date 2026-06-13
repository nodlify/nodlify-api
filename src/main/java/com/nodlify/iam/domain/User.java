package com.nodlify.iam.domain;

import com.nodlify.shared.domain.DisplayName;
import com.nodlify.shared.domain.Email;
import com.nodlify.shared.domain.Identifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;


@With
@Getter
@Entity
@Table(name = "users")
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @EmbeddedId
    private Identifier id;

    @Column(name = "email")
    private Email email;

    @Column(name = "password")
    private Password password;

    @ManyToMany(cascade = {DETACH, MERGE, PERSIST, REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority")
    )
    private final Set<Authority> authorities = new HashSet<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "display_name")
    private DisplayName displayName;

    public User() {
        this.id = Identifier.generate();
    }

    public User(Email email, Password password) {
        this();
        this.password = password;
        this.email = email;
    }

    public User(Email email, Password password, DisplayName displayName) {
        this();
        this.password = password;
        this.email = email;
        this.displayName = displayName;
    }

    public User addAuthority(Authority authority) {
        authorities.add(authority);
        return this;
    }

    public User rename(DisplayName displayName) {
        return this.withDisplayName(displayName);
    }

    public User changePassword(Password password) {
        return this.withPassword(password);
    }

    public List<String> getAuthoritiesAsString() {
        return authorities.stream()
                .map(Authority::value)
                .toList();
    }

    @Override
    public final boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof User that)) return false;
        return id.equals(that.id);
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }
}
