package com.nodlify.iam.application


import com.nodlify.test.stub.ApplicationSetupRepositoryFake
import com.nodlify.test.stub.UserRepositoryFake
import org.springframework.security.crypto.factory.PasswordEncoderFactories


class ApplicationSetupUseCaseTestConfig {

    static final def setupRepo = new ApplicationSetupRepositoryFake()
    static final def userUseCase = new UserService(new UserRepositoryFake(), PasswordEncoderFactories.createDelegatingPasswordEncoder())

    static ApplicationSetupUseCase applicationSetupUseCase() {
        new ApplicationSetupService(setupRepo, userUseCase)
    }
}
