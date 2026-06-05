package com.nodlify.iam.application


import com.nodlify.test.stub.ApplicationSetupRepositoryFake


class ApplicationSetupUseCaseTestConfig {

    static final def setupRepo = new ApplicationSetupRepositoryFake()
    static final def userUseCase = UserUseCaseTestConfig.userUseCase()

    static ApplicationSetupUseCase applicationSetupUseCase() {
        new ApplicationSetupService(setupRepo, userUseCase)
    }
}
