package ai.pesco.delivery.core.application.useCase.commands.createCourier;

import ai.pesco.delivery.core.application.port.repository.UnitOfWorkPort;
import ai.pesco.delivery.core.application.port.repository.domain.CourierRepositoryPort;
import ai.pesco.delivery.core.application.useCase.commands.createOrder.CreateOrderHandlerImpl;
import ai.pesco.delivery.core.domain.model.courierAggregate.Courier;
import ai.pesco.delivery.core.domain.model.courierAggregate.Transport;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

public class CreateCourierHandlerImpl implements CreateCourierHandler {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(CreateOrderHandlerImpl.class);
    private final UnitOfWorkPort unitOfWork;
    private final CourierRepositoryPort courierRepository;

    public CreateCourierHandlerImpl(UnitOfWorkPort unitOfWork, CourierRepositoryPort courierRepository) {
        this.unitOfWork = unitOfWork;
        this.courierRepository = courierRepository;
    }

    @Override
    public void handle(CreateCourierCommand command) {
        unitOfWork.executeInTransaction(() -> {
            Courier newCourier = new Courier(
                    command.name(),
                    new Transport("Car", command.speed()),
                    Location.random()
            );
            courierRepository.add(newCourier);
            LOG.info("Courier with id {} created.", newCourier.getId());
        });
    }
}
