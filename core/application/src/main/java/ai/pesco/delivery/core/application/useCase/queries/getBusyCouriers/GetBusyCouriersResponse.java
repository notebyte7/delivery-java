package ai.pesco.delivery.core.application.useCase.queries.getBusyCouriers;

import java.util.UUID;

public record GetBusyCouriersResponse(UUID id,
                                      String name,
                                      Location location,
                                      UUID transportId) {

    public record Location(int x, int y) {}
}
