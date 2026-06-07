package com.nodlify.iam.application


import com.nodlify.test.fixture.UserFixture
import com.nodlify.test.stub.UserRepositoryFake
import org.springframework.security.crypto.factory.PasswordEncoderFactories


class UserUseCaseTestConfig {

    static final def repo = new UserRepositoryFake()
    static final def passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    static UserUseCase userUseCase() {
        new UserService(repo, passwordEncoder)
    }

    static void setupTestData() {
        repo.save(UserFixture.SOME_ADMIN)
        repo.save(UserFixture.SOME_USER)
    }
}
