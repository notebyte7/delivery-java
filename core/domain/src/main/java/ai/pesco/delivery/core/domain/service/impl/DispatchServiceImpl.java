package ai.pesco.delivery.core.domain.service.impl;

import ai.pesco.delivery.core.domain.model.courierAggregate.Courier;
import ai.pesco.delivery.core.domain.model.courierAggregate.CourierStatus;
import ai.pesco.delivery.core.domain.model.orderAggregate.Order;
import ai.pesco.delivery.core.domain.service.DispatchService;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DispatchServiceImpl implements DispatchService {
    @Override
    public Optional<Courier> dispatch(Order order, Collection<Courier> couriers) {
        if (couriers == null || couriers.isEmpty()) {
            throw new IllegalArgumentException("couriers is null or empty");
        }

        return couriers.stream()
                .filter(courier -> courier.getStatus() == CourierStatus.FREE)
                .min(Comparator.comparingDouble(courier -> calculateDeliveryTime(courier, order)))
                .map(courier -> {
                    courier.assignOrder(order);
                    order.assignToCourier(courier);
                    return courier;
                });
    }

    private double calculateDeliveryTime(Courier courier, Order order) {
        int steps = courier.estimateStepsTo(order.getLocation());
        int speed = courier.getTransport().getSpeed();

        return (double) steps / speed;
    }
}
