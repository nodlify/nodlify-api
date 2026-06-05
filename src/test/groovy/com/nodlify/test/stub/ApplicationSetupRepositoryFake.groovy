package com.nodlify.test.stub

import com.nodlify.iam.domain.ApplicationSetup
import com.nodlify.iam.domain.ApplicationSetupRepository


class ApplicationSetupRepositoryFake implements ApplicationSetupRepository {

    ApplicationSetup applicationSetup

    @Override
    Optional<ApplicationSetup> findAppConfig() {
        Optional.ofNullable(applicationSetup)
    }

    @Override
    ApplicationSetup save(ApplicationSetup config) {
        this.applicationSetup = config
    }
}
