package ai.pesco.delivery.core.application.useCase.queries.getBusyCouriers;

import java.util.List;

public interface GetBusyCouriersHandler {
    List<GetBusyCouriersResponse> handle();
}
