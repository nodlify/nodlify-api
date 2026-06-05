package com.nodlify.observability.problem.application

import com.nodlify.observability.problem.domain.ProblemLog
import com.nodlify.observability.problem.domain.ProblemLogRepository
import com.nodlify.shared.domain.Identifier
import com.nodlify.test.stub.ProblemLogRepositoryFake

class ProblemUseCaseTestConfig {

    public static final Identifier ID = Identifier.of("3b037d40-528c-40d5-879a-bd7212e1b558")
    public static final Identifier ID_2 = Identifier.of("6a9b62c9-d85c-49e4-b06d-8f617eeebf7c")

    static ProblemLogRepository repository = new ProblemLogRepositoryFake()

    static ProblemLogUseCase problemLogUseCase(ProblemLogRepository problemLogRepository = repository) {
        new ProblemLogService(problemLogRepository)
    }

    static void setupTestData() {
        var problem1 = new ProblemLog()
                .withId(ID)
                .withStatus(500)
                .withDetail("ups... app stopped working")
                .withInstance("http://localhost:8080/test/1")
                .withTitle("Internal server error")

        var problem2 = new ProblemLog()
                .withId(ID_2)
                .withStatus(500)
                .withDetail("ups... app stopped working... again")
                .withInstance("http://localhost:8080/test/2")
                .withTitle("Internal server error")

        repository.save(problem1)
        repository.save(problem2)
    }
}
