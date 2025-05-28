package ai.pesco.delivery.core.application.port.repository.domain;

import ai.pesco.delivery.core.domain.model.Aggregate;

import java.util.Collection;
import java.util.UUID;

public interface AggregateRepositoryPort<AGGREGATE extends Aggregate<?>> {

    void add(AGGREGATE aggregate);

    void update(AGGREGATE aggregate);

    void update(Collection<AGGREGATE> aggregates);

    AGGREGATE findById(UUID id);

    Collection<AGGREGATE> findAll();
}
