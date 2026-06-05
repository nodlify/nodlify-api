package com.nodlify.test.stub

import com.nodlify.observability.problem.application.ProblemLogFilter
import com.nodlify.observability.problem.domain.ProblemLog
import com.nodlify.observability.problem.domain.ProblemLogRepository
import com.nodlify.shared.domain.Identifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

import java.util.concurrent.ConcurrentHashMap

class ProblemLogRepositoryFake implements ProblemLogRepository {

    Map<Identifier, ProblemLog> map = new ConcurrentHashMap<>()

    @Override
    ProblemLog save(ProblemLog entity) {
        map.put(entity.id, entity)
        entity
    }

    @Override
    Page<ProblemLog> findAll(Pageable pageable) {
        new PageImpl<ProblemLog>(map.values() as List)
    }

    @Override
    Page<ProblemLog> findAll(ProblemLogFilter filter, Pageable pageable) {
        if (filter == null || filter.empty) {
            return findAll(pageable)
        }
        def result = map.values() as List

        if (filter.title)
            result = result.stream().filter { it.title == filter.title }.toList()

        if (filter.status)
            result = result.stream().filter { it.status == filter.status }.toList()

        if (filter.detail)
            result = result.stream().filter { it.detail == filter.detail }.toList()

        if (filter.instance)
            result = result.stream().filter { it.instance == filter.instance }.toList()

        if (filter.from)
            result = result.stream().filter { it.createdAt <= filter.from }.toList()

        if (filter.to)
            result = result.stream().filter { it.createdAt > filter.to }.toList()

        new PageImpl<ProblemLog>(result)
    }

    @Override
    void deleteById(Identifier problemId) {
        map.remove(problemId)
    }

    @Override
    void deleteAll() {
        map.clear()
    }

    @Override
    Optional<ProblemLog> findById(Identifier identifier) {
        var problem = map[identifier]
        Optional.ofNullable(problem)
    }
}
