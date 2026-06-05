package com.nodlify.observability.problem.application;

import com.nodlify.observability.problem.domain.ProblemLog;
import com.nodlify.observability.problem.domain.ProblemLogRepository;
import com.nodlify.shared.domain.Identifier;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
@CacheConfig(cacheNames = "problems")
class ProblemLogService implements ProblemLogUseCase {

    private final ProblemLogRepository repository;

    @CacheEvict(allEntries = true)
    public void save(ProblemLog entity) {
        repository.save(entity);
    }

    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public Optional<ProblemLog> getProblemById(String id) {
        return repository.findById(Identifier.of(id));
    }

    @Cacheable
    @Transactional(readOnly = true)
    public Page<ProblemLog> getProblems(ProblemLogFilter problemLogFilter, Pageable pageable) {
        return repository.findAll(problemLogFilter, pageable);
    }

    @CacheEvict(allEntries = true)
    public void deleteProblem(String problemId) {
        repository.deleteById(Identifier.of(problemId));
    }

    @CacheEvict(allEntries = true)
    public void deleteAll() {
        repository.deleteAll();
    }
}
