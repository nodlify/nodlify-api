package com.pickdate.iam.application


import com.pickdate.test.stub.ApplicationSetupRepositoryFake


class ApplicationSetupUseCaseTestConfig {

    static final def setupRepo = new ApplicationSetupRepositoryFake()
    static final def userUseCase = UserUseCaseTestConfig.userUseCase()

    static ApplicationSetupUseCase applicationSetupUseCase() {
        new ApplicationSetupService(setupRepo, userUseCase)
    }
}
