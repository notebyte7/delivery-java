package ai.pesco.delivery.core.application.useCase.commands.moveCouriers;

import ai.pesco.delivery.core.application.port.repository.UnitOfWorkPort;
import ai.pesco.delivery.core.application.port.repository.domain.CourierRepositoryPort;
import ai.pesco.delivery.core.application.port.repository.domain.OrderRepositoryPort;
import ai.pesco.delivery.core.domain.model.courierAggregate.Courier;
import ai.pesco.delivery.core.domain.model.orderAggregate.Order;

import java.util.Collection;

public class MoveCouriersHandlerImpl implements MoveCouriersHandler {

    private final OrderRepositoryPort orderRepository;
    private final CourierRepositoryPort courierRepository;
    private final UnitOfWorkPort unitOfWorkPort;

    public MoveCouriersHandlerImpl(OrderRepositoryPort orderRepository,
                                   CourierRepositoryPort courierRepository,
                                   UnitOfWorkPort unitOfWorkPort) {
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
        this.unitOfWorkPort = unitOfWorkPort;
    }

    @Override
    public void handle() {
        unitOfWorkPort.executeInTransaction(() -> {
            Collection<Order> assignedOrders = orderRepository.findAllWithAssignedStatus();
            if (assignedOrders.isEmpty()) {
                throw new RuntimeException("Assigned orders not found.");
            }

            for (Order order : assignedOrders) {
                if (order.getCourierId() == null) {
                    throw new RuntimeException(String.format("There is no courier assigned for the order with id %s.",
                            order.getId())
                    );
                }

                Courier courier = courierRepository.findById(order.getCourierId());
                if (courier == null) {
                    throw new RuntimeException(String.format("Courier with id %s not found.", order.getCourierId()));
                }

                courier.moveTo(order.getLocation());

                if (order.getLocation().equals(courier.getLocation())) {
                    order.complete();
                    courier.completeOrder();
                }

                courierRepository.update(courier);
                orderRepository.update(order);
            }
        });
    }
}
