package ai.pesco.delivery.core.domain.model.courierAggregate;

import ai.pesco.delivery.core.domain.model.orderAggregate.Order;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CourierTest {
    private Courier courier;
    private final String validName = "John";
    private final String validTransportName = "Car";
    private final int validTransportSpeed = 2;
    private final Location validLocation = new Location(1, 1);

    @BeforeEach
    void setUp() {
        courier = new Courier(validName, validTransportName, validTransportSpeed, validLocation);
    }

    @Test
    void constructor_ValidParameters_SetsFieldsCorrectly() {
        assertNotNull(courier.getId());
        assertEquals(validName, courier.getName());
        assertEquals(validLocation, courier.getLocation());
        assertEquals(validTransportName, courier.getTransport().getName());
        assertEquals(validTransportSpeed, courier.getTransport().getSpeed());
        assertEquals(CourierStatus.FREE, courier.getStatus());
        assertNull(courier.getOrderId());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void constructor_InvalidName_ThrowsIllegalArgumentException(String invalidName) {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Courier(invalidName, validTransportName, validTransportSpeed, validLocation));
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @Test
    void constructor_NullLocation_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Courier(validName, validTransportName, validTransportSpeed, null));
        assertEquals("Location cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void rename_InvalidName_ThrowsIllegalArgumentException(String invalidName) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> courier.rename(invalidName));
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @Test
    void rename_ValidName_UpdatesName() {
        String newName = "Jane";
        courier.rename(newName);
        assertEquals(newName, courier.getName());
    }

    @Test
    void assignOrder_ValidOrder_AssignsCorrectly() {
        Order order = new Order(UUID.randomUUID(), new Location(1, 1));
        courier.assignOrder(order);

        assertEquals(order.getId(), courier.getOrderId());
        assertEquals(CourierStatus.BUSY, courier.getStatus());
    }

    @Test
    void assignOrder_NullOrder_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> courier.assignOrder(null));
        assertEquals("Order cannot be null", exception.getMessage());
    }

    @Test
    void assignOrder_NonFreeCourier_ThrowsIllegalStateException() {
        Order order = new Order(UUID.randomUUID(), new Location(1, 1));
        courier.assignOrder(order); // Sets status to BUSY
        Exception exception = assertThrows(IllegalStateException.class, () -> courier.assignOrder(order));
        assertEquals("Cannot assign order to a not free courier", exception.getMessage());
    }

    @Test
    void completeOrder_BusyCourier_CompletesOrder() {
        Order order = new Order(UUID.randomUUID(), new Location(1, 1));
        courier.assignOrder(order);
        courier.completeOrder();

        assertNull(courier.getOrderId());
        assertEquals(CourierStatus.FREE, courier.getStatus());
    }

    @Test
    void completeOrder_NonBusyCourier_ThrowsIllegalStateException() {
        Exception exception = assertThrows(IllegalStateException.class, () -> courier.completeOrder());
        assertEquals("Courier is not busy", exception.getMessage());
    }

    @Test
    void estimateStepsTo_ValidDestination_ReturnsCorrectSteps() {
        Location destination = new Location(2, 1);
        int steps = courier.estimateStepsTo(destination);
        assertEquals(3, steps); // Move to (2,1) takes 3 steps (speed = 2)
    }

    @Test
    void estimateStepsTo_NullDestination_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> courier.estimateStepsTo(null));
        assertEquals("Location cannot be null", exception.getMessage());
    }

    @Test
    void moveTo_ValidDestination_UpdatesLocation() {
        Location destination = new Location(1, 1);
        courier.moveTo(destination);
        Location newLocation = courier.getLocation();

        assertEquals(1, newLocation.getX());
        assertEquals(1, newLocation.getY());
    }

    @Test
    void moveTo_NullDestination_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> courier.moveTo(null));
        assertEquals("Location cannot be null", exception.getMessage());
    }

    @Test
    void equals_SameId_ReturnsTrue() {
        Courier courier1 = new Courier("John", "Car", 2, new Location(2, 2)) {
            @Override
            public UUID getId() {
                return UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            }
        };
        Courier courier2 = new Courier("Jane", "Bike", 3, new Location(1, 1)) {
            @Override
            public UUID getId() {
                return UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            }
        };
        assertEquals(courier1, courier2);
    }

    @Test
    void equals_DifferentId_ReturnsFalse() {
        Courier courier1 = new Courier(validName, validTransportName, validTransportSpeed, validLocation);
        Courier courier2 = new Courier(validName, validTransportName, validTransportSpeed, validLocation);
        assertNotEquals(courier1, courier2); // Different UUIDs
    }
}