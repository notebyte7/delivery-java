package ai.pesco.delivery.core.domain.service;

import ai.pesco.delivery.core.domain.model.courierAggregate.Courier;
import ai.pesco.delivery.core.domain.model.orderAggregate.Order;

import java.util.Collection;
import java.util.Optional;

public interface DispatchService {
    Optional<Courier> dispatch(Order order, Collection<Courier> couriers);
}
