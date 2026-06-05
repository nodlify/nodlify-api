package com.nodlify.test.stub

import com.nodlify.iam.domain.User
import com.nodlify.iam.domain.UserRepository
import com.nodlify.shared.domain.Email
import com.nodlify.shared.domain.Identifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

import java.util.concurrent.ConcurrentHashMap

class UserRepositoryFake implements UserRepository {

    def map = new ConcurrentHashMap<Identifier, User>()

    @Override
    boolean existsByEmail(Email email) {
        map.values().any { it.email == email }
    }

    @Override
    Optional<User> findByEmail(Email email) {
        Optional.ofNullable(map.values().find { it.email == email })
    }

    @Override
    List<User> findAll() {
        map.values() as List
    }

    @Override
    Page<User> findAll(Pageable pageable) {
        new PageImpl<>(map.values() as List)
    }

    @Override
    User save(User user) {
        assert user != null
        map[user.id] = user
        user
    }

    @Override
    Optional<User> findById(Identifier id) {
        Optional.ofNullable(map[id])
    }

    @Override
    void deleteById(Identifier id) {
        map.remove(id)
    }
}
