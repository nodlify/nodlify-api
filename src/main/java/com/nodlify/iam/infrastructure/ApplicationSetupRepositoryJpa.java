package com.nodlify.iam.infrastructure;

import com.nodlify.iam.domain.ApplicationSetup;
import com.nodlify.iam.domain.ApplicationSetupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
interface ApplicationSetupRepositoryJpa extends ApplicationSetupRepository, JpaRepository<ApplicationSetup, String> {

    @Override
    @Query("FROM ApplicationSetup s WHERE s.id = 'app_config'")
    Optional<ApplicationSetup> findAppConfig();

    @Override
    ApplicationSetup save(ApplicationSetup config);
}
