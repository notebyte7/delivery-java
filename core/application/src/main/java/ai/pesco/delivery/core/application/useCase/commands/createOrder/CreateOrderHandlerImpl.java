package ai.pesco.delivery.core.application.useCase.commands.createOrder;

import ai.pesco.delivery.core.application.port.repository.domain.OrderRepositoryPort;
import ai.pesco.delivery.core.domain.model.orderAggregate.Order;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;

public class CreateOrderHandlerImpl implements CreateOrderHandler {
    private final OrderRepositoryPort orderRepository;

    public CreateOrderHandlerImpl(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void handle(CreateOrderCommand command) {
        Order storedOrder = orderRepository.findById(command.basketId());
        if (storedOrder != null) {
            throw new RuntimeException(String.format("Order with id %s already exists", command.basketId()));
        }

        Order order = new Order(command.basketId(), Location.random());
        orderRepository.add(order);
    }
}
