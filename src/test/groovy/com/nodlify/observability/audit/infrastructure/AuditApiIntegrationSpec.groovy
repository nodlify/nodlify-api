package com.nodlify.observability.audit.infrastructure

import com.nodlify.observability.audit.application.AuditEventUseCaseConfig
import org.springframework.data.domain.Pageable
import spock.lang.Execution
import spock.lang.Specification

import static com.nodlify.observability.audit.application.AuditEventUseCaseConfig.setupTestData
import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD


@Execution(SAME_THREAD)
class AuditApiIntegrationSpec extends Specification {

    def auditEventUseCase = AuditEventUseCaseConfig.auditEventUseCase()
    def api = new AuditApi(auditEventUseCase)

    def setup() {
        setupTestData()
    }

    def "should get audits by filters"() {
        given:
        def action = "login_failed"
        def user = "admin@test.email"

        when:
        def response = api.getAudits(Pageable.ofSize(2), action, user, null, null)

        then:
        def actual = response.content.first
        actual.action() == action
        actual.user() == user
        actual.remoteAddress() == null
        actual.userAgent() == null
        actual.payload() == null

        and:
        actual.id()
    }

    def "should find audit by id"() {
        given:
        def auditId = "6cde39e7-fffa-46dc-98ed-3b462c933632"

        when:
        def responseEntity = api.getAudit(auditId)

        then:
        responseEntity.statusCode.'2xxSuccessful'

        and:
        def audit = responseEntity.body
        audit.user() == "user@test.email"
    }

    def "should delete all"() {
        when:
        api.deleteAll()

        then:
        api.getAudits(Pageable.unpaged(), null, null, null, null)
                .metadata.size() == 0
    }

    def "should delete audit by id"() {
        given:
        def auditId = "6cde39e7-fffa-46dc-98ed-3b462c933632"

        when:
        def responseEntity = api.delete(auditId)

        then:
        responseEntity.statusCode.value() == 204
    }
}
