package ai.pesco.delivery.core.domain.model.courierAggregate;

import ai.pesco.delivery.core.domain.model.Aggregate;
import ai.pesco.delivery.core.domain.model.orderAggregate.Order;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Courier extends Aggregate<UUID> {
    private String name;
    private Transport transport;
    private Location location;
    private CourierStatus status;
    private UUID orderId;

    public Courier(String name, Transport transport, Location location) {
        checkLocation(location);

        super.id = UUID.randomUUID();
        this.name = name;
        this.location = location;
        this.transport = transport;
        status = CourierStatus.FREE;
    }

    public void assignOrder(Order order) {
       if (order == null) {
           throw new IllegalArgumentException("order is null");
       }
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

    public void rename(String name) {
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
