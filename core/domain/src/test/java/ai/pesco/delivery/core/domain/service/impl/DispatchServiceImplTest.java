package ai.pesco.delivery.core.domain.service.impl;

import ai.pesco.delivery.core.domain.model.courierAggregate.Courier;
import ai.pesco.delivery.core.domain.model.courierAggregate.CourierStatus;
import ai.pesco.delivery.core.domain.model.orderAggregate.Order;
import ai.pesco.delivery.core.domain.model.orderAggregate.OrderStatus;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import ai.pesco.delivery.core.domain.service.DispatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DispatchServiceImplTest {

    private DispatchService dispatchService;
    private Order order;
    private Location orderLocation;

    @BeforeEach
    void setUp() {
        dispatchService = new DispatchServiceImpl();
        orderLocation = new Location(5, 5);
        order = new Order(UUID.randomUUID(), orderLocation);
    }

    @Test
    void dispatch_ShouldThrowException_WhenCouriersIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> dispatchService.dispatch(order, null),
                "couriers is null or empty");
    }

    @Test
    void dispatch_ShouldThrowException_WhenCouriersIsEmpty() {
        Collection<Courier> emptyCouriers = Collections.emptyList();

        assertThrows(IllegalArgumentException.class,
                () -> dispatchService.dispatch(order, emptyCouriers),
                "couriers is null or empty");
    }

    @Test
    void dispatch_ShouldReturnEmpty_WhenNoCouriersAreFree() {
        Courier busyCourier1 = new Courier("John", "Bike", 2, new Location(3, 3));
        Courier busyCourier2 = new Courier("Jane", "Car", 3, new Location(7, 7));

        Order dummyOrder1 = new Order(UUID.randomUUID(), new Location(1, 1));
        Order dummyOrder2 = new Order(UUID.randomUUID(), new Location(2, 2));
        busyCourier1.assignOrder(dummyOrder1);
        busyCourier2.assignOrder(dummyOrder2);

        Collection<Courier> couriers = Arrays.asList(busyCourier1, busyCourier2);

        Optional<Courier> result = dispatchService.dispatch(order, couriers);

        assertFalse(result.isPresent());
    }

    @Test
    void dispatch_ShouldReturnNearestFreeCourier_WhenMultipleCouriersAreFree() {
        Courier farCourier = new Courier("John", "Bike", 2, new Location(10, 10));
        Courier nearCourier = new Courier("Jane", "Car", 3, new Location(6, 6));
        Courier mediumCourier = new Courier("Bob", "Scooter", 2, new Location(8, 8));

        Collection<Courier> couriers = Arrays.asList(farCourier, nearCourier, mediumCourier);

        Optional<Courier> result = dispatchService.dispatch(order, couriers);

        assertTrue(result.isPresent());
        assertEquals(nearCourier, result.get());
        assertEquals(CourierStatus.BUSY, nearCourier.getStatus());
        assertEquals(order.getId(), nearCourier.getOrderId());
        assertEquals(OrderStatus.ASSIGNED, order.getStatus());
        assertEquals(nearCourier.getId(), order.getCourierId());
    }

    @Test
    void dispatch_ShouldReturnTheOnlyFreeCourier_WhenOnlyOneIsFree() {
        Courier freeCourier = new Courier("John", "Bike", 2, new Location(3, 3));
        Courier busyCourier = new Courier("Jane", "Car", 3, new Location(1, 1));

        Order dummyOrder = new Order(UUID.randomUUID(), new Location(2, 2));
        busyCourier.assignOrder(dummyOrder);

        Collection<Courier> couriers = Arrays.asList(freeCourier, busyCourier);

        Optional<Courier> result = dispatchService.dispatch(order, couriers);

        assertTrue(result.isPresent());
        assertEquals(freeCourier, result.get());
        assertEquals(CourierStatus.BUSY, freeCourier.getStatus());
        assertEquals(order.getId(), freeCourier.getOrderId());
    }


    @Test
    void dispatch_ShouldWorkWithDifferentTransportSpeeds() {
        Location courierLocation = new Location(1, 1);
        Courier slowCourier = new Courier("Slow", "Walk", 1, courierLocation);
        Courier mediumCourier = new Courier("Medium", "Bike", 2, courierLocation);
        Courier fastCourier = new Courier("Fast", "Car", 3, courierLocation);

        Collection<Courier> couriers = Arrays.asList(slowCourier, mediumCourier, fastCourier);

        Optional<Courier> result = dispatchService.dispatch(order, couriers);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    void dispatch_ShouldHandleMixOfFreeAndBusyCouriers() {
        Courier nearButBusy = new Courier("Near Busy", "Car", 3, new Location(5, 6));
        Courier farButFree = new Courier("Far Free", "Bike", 2, new Location(10, 10));
        Courier mediumAndFree = new Courier("Medium Free", "Scooter", 2, new Location(7, 7));

        Order dummyOrder = new Order(UUID.randomUUID(), new Location(1, 1));
        nearButBusy.assignOrder(dummyOrder);

        Collection<Courier> couriers = Arrays.asList(nearButBusy, farButFree, mediumAndFree);

        Optional<Courier> result = dispatchService.dispatch(order, couriers);

        assertTrue(result.isPresent());
        assertEquals(mediumAndFree, result.get(), "Should select nearest free courier");
    }

    @Test
    void dispatch_ShouldMaintainOrderAssignment() {
        Courier courier = new Courier("Test", "Bike", 2, new Location(3, 3));
        Collection<Courier> couriers = Collections.singletonList(courier);

        Optional<Courier> result = dispatchService.dispatch(order, couriers);

        assertTrue(result.isPresent());
        assertEquals(order.getId(), courier.getOrderId());
        assertEquals(courier.getId(), order.getCourierId());
        assertEquals(CourierStatus.BUSY, courier.getStatus());
        assertEquals(OrderStatus.ASSIGNED, order.getStatus());
    }
}