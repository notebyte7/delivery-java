package ai.pesco.delivery.core.domain.model.courierAggregate;

import ai.pesco.delivery.core.domain.model.orderAggregate.Order;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
public class Courier {
    private final UUID id;
    private String name;
    private Transport transport;
    private Location location;
    private CourierStatus status;
    private UUID orderId;

    public Courier(String name, String transportName, int transportSpeed, Location location) {
        checkName(name);
        checkLocation(location);

        id = UUID.randomUUID();
        this.name = name;
        this.location = location;
        transport = new Transport(transportName, transportSpeed);
        status = CourierStatus.FREE;
    }

    public void assignOrder(Order order) {
        checkOrder(order);
        if (status != CourierStatus.FREE) {
            throw new IllegalStateException("Cannot assign order to a not free courier");
        }

        orderId = order.getId();
        status = CourierStatus.BUSY;
    }

    public void completeOrder() {
        if (status != CourierStatus.BUSY) {
            throw new IllegalStateException("Courier is not busy");
        }

        orderId = null;
        status = CourierStatus.FREE;
    }

    public int estimateStepsTo(Location destination) {
        checkLocation(destination);

        Location move = transport.move(location, destination);
        return Math.abs(move.getX()) + Math.abs(move.getY());
    }

    public void moveTo(Location destination) {
        checkLocation(destination);
        location = transport.move(location, destination);
    }

    private void checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    public void rename(String name) {
        checkName(name);
        this.name = name;
    }

    private void changeTransport(String transportName, int speed) {
        transport = new Transport(transportName, speed);
    }

    private void checkLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
    }

    private void checkOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
    }
}
