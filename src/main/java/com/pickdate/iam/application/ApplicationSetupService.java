package com.pickdate.iam.application;

import com.pickdate.iam.domain.ApplicationSetup;
import com.pickdate.iam.domain.ApplicationSetupRepository;
import com.pickdate.iam.domain.DomainUrl;
import com.pickdate.iam.domain.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.pickdate.iam.domain.Authority.ADMIN;


@Service
@RequiredArgsConstructor
class ApplicationSetupService implements ApplicationSetupUseCase {

    static final String CACHE_SETUP = "app-setup";

    private final ApplicationSetupRepository applicationSetupRepository;
    private final UserUseCase userUseCase;

    @Override
    @Transactional
    @CacheEvict(value = CACHE_SETUP, allEntries = true)
    public void setupDomain(DomainUrl domainUrl) {
        ApplicationSetup config = createOrFetchApplicationSetup();
        config.setDomainUrl(domainUrl);
        applicationSetupRepository.save(config);
    }

    @Override
    @Transactional
    public void setupAdmin(User user) {
        user.addAuthority(ADMIN);
        userUseCase.createUser(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_SETUP, allEntries = true)
    public void completeSetup() {
        ApplicationSetup appConfig = createOrFetchApplicationSetup();
        if (appConfig.isSetupCompleted()) {
            return;
        }
        appConfig.completeSetup();
        applicationSetupRepository.save(appConfig);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_SETUP)
    public boolean setupCompleted() {
        return applicationSetupRepository.findAppConfig()
                .map(ApplicationSetup::isSetupCompleted)
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> getDomainUrl() {
        return applicationSetupRepository.findAppConfig()
                .map(ApplicationSetup::getDomainUrl)
                .map(DomainUrl::getValue);
    }

    private @NonNull ApplicationSetup createOrFetchApplicationSetup() {
        return applicationSetupRepository.findAppConfig()
                .orElseGet(ApplicationSetup::new);
    }
}
