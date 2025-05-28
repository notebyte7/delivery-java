package ai.pesco.delivery.core.domain.service.impl;

import ai.pesco.delivery.core.domain.model.courierAggregate.Courier;
import ai.pesco.delivery.core.domain.model.courierAggregate.CourierStatus;
import ai.pesco.delivery.core.domain.model.orderAggregate.Order;
import ai.pesco.delivery.core.domain.model.orderAggregate.OrderStatus;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DispatchServiceImplTest {

    private DispatchServiceImpl dispatchService;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        dispatchService = new DispatchServiceImpl();
        testOrder = new Order(UUID.randomUUID(), new Location(5, 5));
    }

    @Nested
    @DisplayName("Успешное назначение курьера")
    class SuccessfulDispatch {

        @Test
        @DisplayName("Должен назначить единственного свободного курьера")
        void shouldDispatchSingleFreeCourier() {
            Courier courier = new Courier("John", "bike", 3, new Location(3, 3));
            List<Courier> couriers = List.of(courier);

            Optional<Courier> result = dispatchService.dispatch(testOrder, couriers);

            assertTrue(result.isPresent());
            assertEquals(courier, result.get());
            assertEquals(CourierStatus.BUSY, courier.getStatus());
            assertEquals(testOrder.getId(), courier.getOrderId());
            assertEquals(OrderStatus.ASSIGNED, testOrder.getStatus());
            assertEquals(courier.getId(), testOrder.getCourierId());
        }

        @Test
        @DisplayName("Должен выбрать ближайшего курьера из нескольких свободных")
        void shouldDispatchClosestCourier() {
            Location orderLocation = new Location(5, 5);
            Order order = new Order(UUID.randomUUID(), orderLocation);

            Courier closeCourier = new Courier("Close", "bike", 2, new Location(6, 6));
            Courier farCourier = new Courier("Far", "car", 3, new Location(1, 1));
            Courier middleCourier = new Courier("Middle", "walk", 1, new Location(7, 5));

            List<Courier> couriers = Arrays.asList(farCourier, closeCourier, middleCourier);

            Optional<Courier> result = dispatchService.dispatch(order, couriers);

            assertTrue(result.isPresent());
            assertEquals(2, result.get().estimateStepsTo(orderLocation));
        }

        @Test
        @DisplayName("Должен игнорировать занятых курьеров и выбрать свободного")
        void shouldIgnoreBusyCouriersAndDispatchFreeCourier() {
            Courier busyCourier = new Courier("Busy", "bike", 2, new Location(4, 4));
            Courier freeCourier = new Courier("Free", "car", 3, new Location(8, 8));

            Order otherOrder = new Order(UUID.randomUUID(), new Location(1, 1));
            busyCourier.assignOrder(otherOrder);

            List<Courier> couriers = Arrays.asList(busyCourier, freeCourier);

            Optional<Courier> result = dispatchService.dispatch(testOrder, couriers);

            assertTrue(result.isPresent());
            assertEquals(freeCourier, result.get());
            assertEquals(CourierStatus.BUSY, freeCourier.getStatus());
            assertEquals(CourierStatus.BUSY, busyCourier.getStatus());
        }

        @Test
        @DisplayName("Должен выбрать первого среди курьеров с одинаковым расстоянием")
        void shouldDispatchFirstCourierWhenDistancesAreEqual() {
            Location orderLocation = new Location(5, 5);
            Order order = new Order(UUID.randomUUID(), orderLocation);

            Courier courier1 = new Courier("First", "bike", 2, new Location(7, 5));
            Courier courier2 = new Courier("Second", "car", 3, new Location(5, 7));
            Courier courier3 = new Courier("Third", "walk", 1, new Location(1, 1));

            List<Courier> couriers = Arrays.asList(courier1, courier2, courier3);

            Optional<Courier> result = dispatchService.dispatch(order, couriers);

            assertTrue(result.isPresent());
            assertEquals(courier2, result.get());
            assertEquals(2, result.get().estimateStepsTo(orderLocation));
        }
    }

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCases {

        @Test
        @DisplayName("Должен работать с курьерами в одинаковой локации с заказом")
        void shouldHandleCouriersAtSameLocationAsOrder() {
            Location orderLocation = new Location(5, 5);
            Order order = new Order(UUID.randomUUID(), orderLocation);

            Courier courierAtSameLocation = new Courier("Same", "bike", 2, orderLocation);
            Courier courierNearby = new Courier("Nearby", "car", 3, new Location(6, 6));

            List<Courier> couriers = Arrays.asList(courierAtSameLocation, courierNearby);

            Optional<Courier> result = dispatchService.dispatch(order, couriers);

            assertTrue(result.isPresent());
            assertEquals(courierAtSameLocation, result.get());
            assertEquals(0, result.get().estimateStepsTo(orderLocation));
        }


        @Test
        @DisplayName("Должен правильно обновить состояние заказа и курьера")
        void shouldCorrectlyUpdateOrderAndCourierState() {
            Courier courier = new Courier("Test", "bike", 3, new Location(3, 3));
            List<Courier> couriers = List.of(courier);

            UUID initialOrderId = testOrder.getId();
            UUID initialCourierId = courier.getId();

            Optional<Courier> result = dispatchService.dispatch(testOrder, couriers);

            assertTrue(result.isPresent());

            assertEquals(CourierStatus.BUSY, courier.getStatus());
            assertEquals(initialOrderId, courier.getOrderId());

            assertEquals(OrderStatus.ASSIGNED, testOrder.getStatus());
            assertEquals(initialCourierId, testOrder.getCourierId());
        }
    }
}