package ai.pesco.delivery.core.domain.model.orderAggregate;

import ai.pesco.delivery.core.domain.model.courierAggregate.Courier;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
public class Order {
    private final UUID id;
    private Location location;
    private OrderStatus status;
    private UUID courierId;

    public Order(UUID id, Location location) {
        checkId(id);
        checkLocation(location);

        this.id = id;
        this.location = location;
        status = OrderStatus.CREATED;
    }

    public void assignToCourier(Courier courier) {
        if (status != OrderStatus.CREATED) {
            throw new IllegalArgumentException("To be assigned, the order must have the CREATED status");
        }
        checkCourier(courier);

        courierId = courier.getId();
        status = OrderStatus.ASSIGNED;
    }

    public void relocate(Location location) {
        checkLocation(location);
        this.location = location;
    }

    public void complete() {
        if (status != OrderStatus.ASSIGNED) {
            throw new IllegalStateException("Only assigned orders can be completed");
        }
        status = OrderStatus.COMPLETED;
    }

    private void checkId(UUID id) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }
    }

    private void checkLocation(Location location) {
        if (location == null) {
            throw new NullPointerException("location is null");
        }
    }

    private void checkCourier(Courier courier) {
        if (courier == null) {
            throw new NullPointerException("courier is null");
        }
    }

}
