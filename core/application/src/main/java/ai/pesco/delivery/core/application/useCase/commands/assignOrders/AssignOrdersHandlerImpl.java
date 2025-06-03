package ai.pesco.delivery.core.application.useCase.commands.assignOrders;

import ai.pesco.delivery.core.application.port.repository.UnitOfWorkPort;
import ai.pesco.delivery.core.application.port.repository.domain.CourierRepositoryPort;
import ai.pesco.delivery.core.application.port.repository.domain.OrderRepositoryPort;
import ai.pesco.delivery.core.domain.model.courierAggregate.Courier;
import ai.pesco.delivery.core.domain.model.orderAggregate.Order;
import ai.pesco.delivery.core.domain.service.DispatchService;

import java.util.Collection;
import java.util.Optional;

public class AssignOrdersHandlerImpl implements AssignOrdersHandler {
    private final CourierRepositoryPort courierRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final DispatchService dispatchService;
    private final UnitOfWorkPort unitOfWork;

    public AssignOrdersHandlerImpl(CourierRepositoryPort courierRepositoryPort,
                                   OrderRepositoryPort orderRepositoryPort,
                                   DispatchService dispatchService,
                                   UnitOfWorkPort unitOfWork) {
        this.courierRepositoryPort = courierRepositoryPort;
        this.orderRepositoryPort = orderRepositoryPort;
        this.dispatchService = dispatchService;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public void handle() {
        unitOfWork.executeInTransaction(() -> {
            Order order = orderRepositoryPort.findAnyWithCreatedStatus();
            Collection<Courier> availableCouriers = courierRepositoryPort.findAllWithFreeStatus();
            Optional<Courier> designatedCourier = dispatchService.dispatch(order, availableCouriers);

            if (designatedCourier != null) {
                courierRepositoryPort.update(designatedCourier);
                orderRepositoryPort.update(order);
            }
        });
    }
}
