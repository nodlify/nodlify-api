package com.nodlify.iam.infrastructure;

import com.nodlify.iam.domain.UserRepository;
import com.nodlify.shared.domain.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
class UserAuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String userEmail) throws UsernameNotFoundException {
        return userRepository.findByEmail(Email.of(userEmail))
                .map(UserAuthentication::from)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
