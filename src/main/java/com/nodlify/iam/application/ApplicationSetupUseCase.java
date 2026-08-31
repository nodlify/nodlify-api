package com.nodlify.iam.application;


import com.nodlify.iam.domain.DomainUrl;

import java.util.Optional;

public interface ApplicationSetupUseCase {

    void setupDomain(DomainUrl domainUrl);

    boolean setupCompleted();

    Optional<String> getDomainUrl();

    void completeSetup();

    void setupAdmin(CreateUserCommand command);
}
