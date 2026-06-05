package com.nodlify.iam.domain;

import java.io.Serializable;
import java.util.Optional;


public interface ApplicationSetupRepository extends Serializable {

    Optional<ApplicationSetup> findAppConfig();

    ApplicationSetup save(ApplicationSetup config);
}
