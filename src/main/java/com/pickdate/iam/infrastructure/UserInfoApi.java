package com.pickdate.iam.infrastructure;

import com.pickdate.iam.application.UserUseCase;
import com.pickdate.iam.domain.User;
import com.pickdate.iam.domain.UserData;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "Public - Users", description = "User profile endpoints")
class UserInfoApi {

    private static final ResponseEntity<UserData> NO_CONTENT = ResponseEntity.noContent().build();

    private final UserUseCase userUseCase;

    @GetMapping("/api/v1/userinfo")
    ResponseEntity<UserData> getUserInfo(Authentication authentication) {

        if (isAnonymous(authentication)) {
            return NO_CONTENT;
        }
        return getUser(authentication)
                .map(UserData::from)
                .map(ResponseEntity::ok)
                .orElse(NO_CONTENT);
    }

    private boolean isAnonymous(Authentication authentication) {
        return authentication == null
                || authentication.getPrincipal() == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken;
    }

    private Optional<User> getUser(Authentication authentication) {
        try {
            return Optional.ofNullable(userUseCase.getUserByEmail(authentication.getName()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
