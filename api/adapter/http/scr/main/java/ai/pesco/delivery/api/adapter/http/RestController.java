package ai.pesco.delivery.api.adapter.http;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RestController  {
    private final CreateCourierHandler createCourierHandler;
    private final CreateOrderHandler createOrderHandler;
    private final GetBusyCouriersHandler getBusyCouriersHandler;
    private final GetNotCompletedOrdersHandler getNotCompletedOrdersHandler;

    public ResponseEntity<Void> createCourier(NewCourier newCourier) {
        createCourierHandler.handle(new CreateCourierCommand(newCourier.getName(), newCourier.getSpeed()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<Void> createOrder() {
        UUID orderId = UUID.randomUUID();
        String street = "Несуществующая";
        CreateOrderCommand orderCommand = new CreateOrderCommand(orderId, street);
        createOrderHandler.handle(orderCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<List<Courier>> getCouriers() {
        return ResponseEntity.ok(
                getBusyCouriersHandler.handle().stream()
                        .map(busyCourier -> new Courier(
                                busyCourier.id(),
                                busyCourier.name(),
                                new Location(busyCourier.location().x(), busyCourier.location().y())
                        ))
                        .collect(Collectors.toList())
        );
    }

    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok(
                getNotCompletedOrdersHandler.handle().stream()
                        .map(notCompletedOrder -> new Order(
                                notCompletedOrder.id(),
                                new Location(notCompletedOrder.location().x(), notCompletedOrder.location().y())
                        ))
                        .collect(Collectors.toList())
        );
    }
}
}
