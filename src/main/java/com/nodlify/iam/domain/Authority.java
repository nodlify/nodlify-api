package com.nodlify.iam.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;


@Entity
@Table(name = "authorities")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Authority {

    public static final Authority USER = new Authority("USER");
    public static final Authority ADMIN = new Authority("ADMIN");

    @Id
    private String authority;

    @CreatedDate
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    private Instant updatedAt;

    Authority(String authority) {
        this.authority = authority;
    }

    public static Authority of(String authority) {
        return new Authority(authority);
    }

    public String value() {
        return authority;
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Authority auth)) return false;
        return Objects.equals(authority, auth.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authority);
    }

    @Override
    public String toString() {
        return authority;
    }
}
