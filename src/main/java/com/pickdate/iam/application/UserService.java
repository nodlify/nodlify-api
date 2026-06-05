package com.pickdate.iam.application;

import com.pickdate.iam.domain.Authority;
import com.pickdate.iam.domain.User;
import com.pickdate.iam.domain.UserNotFoundException;
import com.pickdate.iam.domain.UserRepository;
import com.pickdate.shared.domain.Email;
import com.pickdate.shared.domain.Identifier;
import com.pickdate.shared.domain.Property;
import com.pickdate.shared.exception.ResourceAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
class UserService implements UserUseCase {

    static final String CACHE_BY_ID = "users-by-id";
    static final String CACHE_BY_EMAIL = "users-by-email";

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_BY_ID, key = "#id")
    public User getUserById(String id) {
        return getByIdOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_BY_EMAIL, key = "#email")
    public User getUserByEmail(String email) {
        return getByEmailOrThrow(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_BY_ID, allEntries = true),
            @CacheEvict(value = CACHE_BY_EMAIL, allEntries = true)
    })
    public User createUser(User user) {
        assertEmailNotTaken(user.getEmail());
        user.addAuthority(Authority.USER);
        return userRepository.save(user);
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
