package ai.pesco.delivery.core.domain.model.orderAggregate;

import ai.pesco.delivery.core.domain.model.courierAggregate.Courier;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private Order order;
    private UUID validId;
    private Location validLocation;

    @BeforeEach
    void setUp() {
        validId = UUID.randomUUID();
        validLocation = new Location(1, 1);
        order = new Order(validId, validLocation);
    }

    @Test
    void constructor_ValidParameters_SetsFieldsCorrectly() {
        assertEquals(validId, order.getId());
        assertEquals(validLocation, order.getLocation());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertNull(order.getCourierId());
    }

    @Test
    void constructor_NullId_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () -> new Order(null, validLocation));
        assertEquals("id is null", exception.getMessage());
    }

    @Test
    void constructor_NullLocation_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () -> new Order(validId, null));
        assertEquals("location is null", exception.getMessage());
    }

    @Test
    void assignToCourier_ValidCourier_AssignsCorrectly() {
        Courier courier = new Courier("John", "Car", 2, new Location(1, 1));
        order.assignToCourier(courier);

        assertEquals(courier.getId(), order.getCourierId());
        assertEquals(OrderStatus.ASSIGNED, order.getStatus());
    }

    @Test
    void assignToCourier_NullCourier_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () -> order.assignToCourier(null));
        assertEquals("courier is null", exception.getMessage());
    }

    @Test
    void assignToCourier_NonCreatedStatus_ThrowsIllegalArgumentException() {
        Courier courier = new Courier("John", "Car", 2, new Location(1, 1));
        order.assignToCourier(courier); // Sets status to ASSIGNED
        Exception exception = assertThrows(IllegalArgumentException.class, () -> order.assignToCourier(courier));
        assertEquals("To be assigned, the order must have the CREATED status", exception.getMessage());
    }

    @Test
    void relocate_ValidLocation_UpdatesLocation() {
        Location newLocation = new Location(10, 10);
        order.relocate(newLocation);
        assertEquals(newLocation, order.getLocation());
    }

    @Test
    void relocate_NullLocation_ThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () -> order.relocate(null));
        assertEquals("location is null", exception.getMessage());
    }

    @Test
    void complete_AssignedOrder_CompletesOrder() {
        Courier courier = new Courier("John", "Car", 2, new Location(1, 1));
        order.assignToCourier(courier);
        order.complete();
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    void complete_NonAssignedOrder_ThrowsIllegalStateException() {
        Exception exception = assertThrows(IllegalStateException.class, () -> order.complete());
        assertEquals("Only assigned orders can be completed", exception.getMessage());
    }

    @Test
    void equals_SameId_ReturnsTrue() {
        Order order1 = new Order(validId, new Location(1, 1));
        Order order2 = new Order(validId, new Location(5, 2));
        assertEquals(order1, order2);
    }

    @Test
    void equals_DifferentId_ReturnsFalse() {
        Order order1 = new Order(UUID.randomUUID(), validLocation);
        Order order2 = new Order(UUID.randomUUID(), validLocation);
        assertNotEquals(order1, order2);
    }
}