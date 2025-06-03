package ai.pesco.delivery.core.application.useCase.queries.getNotCompletedOrders;

import java.util.UUID;

public record GetNotCompletedOrdersResponse(UUID id, Location location) {
    public record Location(int x, int y) {}
}
