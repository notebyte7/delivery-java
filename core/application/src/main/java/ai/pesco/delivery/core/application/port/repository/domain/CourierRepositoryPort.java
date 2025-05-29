package ai.pesco.delivery.core.application.port.repository.domain;

import ai.pesco.delivery.core.domain.model.courierAggregate.Courier;

import java.util.Collection;

public interface CourierRepositoryPort extends AggregateRepositoryPort<Courier>{
    Collection<Courier> findAllWithFreeStatus();
}
