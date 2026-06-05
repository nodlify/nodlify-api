package com.nodlify.iam.domain;

import com.nodlify.shared.domain.Email;
import com.nodlify.shared.domain.Identifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;


public interface UserRepository {

    boolean existsByEmail(Email email);

    Optional<User> findByEmail(Email email);

    Page<User> findAll(Pageable pageable);

    Collection<User> findAll();

    User save(User user);

    Optional<User> findById(Identifier id);

    void deleteById(Identifier identifier);
}
