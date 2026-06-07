package com.nodlify.iam.application;

import com.nodlify.iam.domain.*;
import com.nodlify.shared.domain.DisplayName;
import com.nodlify.shared.domain.Email;
import com.nodlify.shared.domain.Identifier;
import com.nodlify.shared.domain.Property;
import com.nodlify.shared.exception.IllegalValueException;
import com.nodlify.shared.exception.ResourceAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
class UserService implements UserUseCase {

    static final String CACHE_BY_ID = "users-by-id";
    static final String CACHE_BY_EMAIL = "users-by-email";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_BY_ID, key = "#id")
    public UserData getUserById(String id) {
        return UserData.from(getByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_BY_EMAIL, key = "#email")
    public UserData getUserByEmail(String email) {
        return UserData.from(getByEmailOrThrow(email));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserData> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserData::from);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_BY_ID, allEntries = true),
            @CacheEvict(value = CACHE_BY_EMAIL, allEntries = true)
    })
    public UserData createUser(CreateUserCommand command) {
        return create(command, Authority.USER);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_BY_ID, allEntries = true),
            @CacheEvict(value = CACHE_BY_EMAIL, allEntries = true)
    })
    public UserData createAdmin(CreateUserCommand command) {
        return create(command, Authority.USER, Authority.ADMIN);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_BY_ID, key = "#id"),
            @CacheEvict(value = CACHE_BY_EMAIL, allEntries = true)
    })
    public void deleteUser(String id) {
        userRepository.deleteById(Identifier.of(id));
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_BY_ID, allEntries = true),
            @CacheEvict(value = CACHE_BY_EMAIL, allEntries = true)
    })
    public UserData changeDisplayName(String email, ChangeDisplayNameCommand command) {
        var user = getByEmailOrThrow(email);
        var renamed = user.rename(DisplayName.of(command.displayName()));
        return UserData.from(userRepository.save(renamed));
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_BY_ID, allEntries = true),
            @CacheEvict(value = CACHE_BY_EMAIL, allEntries = true)
    })
    public void changePassword(String email, ChangePasswordCommand command) {
        var user = getByEmailOrThrow(email);
        assertCurrentPasswordMatches(command.currentPassword(), user.getPassword());
        var updated = user.changePassword(Password.fromPlaintext(command.newPassword()));
        userRepository.save(updated);
    }

    private UserData create(CreateUserCommand command, Authority... roles) {
        var user = new User(
                Email.of(command.email()),
                Password.fromPlaintext(command.password()),
                DisplayName.of(command.displayName())
        );
        assertEmailNotTaken(user.getEmail());
        for (Authority role : roles) {
            user.addAuthority(role);
        }
        return UserData.from(userRepository.save(user));
    }

    private void assertCurrentPasswordMatches(String currentPassword, Password storedPassword) {
        if (currentPassword == null || !passwordEncoder.matches(currentPassword, storedPassword.getValue())) {
            throw new IllegalValueException(
                    Property.of("currentPassword", "***"), "Current password is incorrect"
            );
        }
    }

    private void assertEmailNotTaken(Email email) {
        if (userRepository.existsByEmail(email))
            throw new ResourceAlreadyExistException(
                    Property.of("email", email), "User with email %s already exists".formatted(email)
            );
    }

    private User getByEmailOrThrow(String email) {
        return userRepository.findByEmail(Email.of(email))
                .orElseThrow(() -> UserNotFoundException.withEmail(email));
    }

    private User getByIdOrThrow(String id) {
        return userRepository.findById(Identifier.of(id))
                .orElseThrow(() -> UserNotFoundException.withId(id));
    }
}
