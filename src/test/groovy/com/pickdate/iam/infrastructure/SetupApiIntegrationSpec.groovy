package com.pickdate.iam.infrastructure

import com.pickdate.iam.application.ApplicationSetupUseCaseTestConfig
import spock.lang.Specification
import spock.lang.Subject


class SetupApiIntegrationSpec extends Specification {

    def applicationSetupUseCase = ApplicationSetupUseCaseTestConfig.applicationSetupUseCase()

    @Subject
    def setupApi = new SetupApi(applicationSetupUseCase)

    def "should setup domain"() {
        given:
        def request = new SetupDomainRequest("http://localhost:8080")

        when:
        def responseEntity = setupApi.setupDomain(request)

        then:
        responseEntity.statusCode.is2xxSuccessful()
    }

    def "should create initial admin user"() {
        given:
        def request = new CreateUserRequest("admin@email.com", "superSecretPassword!", "Admin")

        when:
        def responseEntity = setupApi.initializeAdminUser(request)

        then:
        responseEntity.statusCode.is2xxSuccessful()
    }

    def "should complete setup"() {
        when:
        def responseEntity = setupApi.completeSetup()

        then:
        responseEntity.statusCode.is2xxSuccessful()
    }
}
