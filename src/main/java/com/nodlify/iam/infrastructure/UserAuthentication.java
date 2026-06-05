package com.nodlify.iam.infrastructure;

import com.nodlify.iam.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

import static java.util.stream.Collectors.toSet;


@With
@Data
@NoArgsConstructor
@AllArgsConstructor
class UserAuthentication implements UserDetails, CredentialsContainer {

    private String email;
    private String password;
    private Set<GrantedAuthority> authorities;

    static UserDetails from(User user) {
        return new UserAuthentication()
                .withEmail(user.getEmail().getValue())
                .withPassword(user.getPassword().getValue())
                .withAuthorities(user.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.value()))
                        .collect(toSet())
                );
    }

    @Override
    public @NonNull String getUsername() {
        return email;
    }

    @Override
    public void eraseCredentials() {
        this.password = "";
    }
}
