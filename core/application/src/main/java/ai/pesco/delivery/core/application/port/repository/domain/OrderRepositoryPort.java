package ai.pesco.delivery.core.application.port.repository.domain;

import ai.pesco.delivery.core.domain.model.orderAggregate.Order;

import java.util.Collection;

public interface OrderRepositoryPort extends AggregateRepositoryPort<Order> {
    Order findAnyWithCreatedStatus();
    Collection<Order> findAllWithAssignedStatus();
}
