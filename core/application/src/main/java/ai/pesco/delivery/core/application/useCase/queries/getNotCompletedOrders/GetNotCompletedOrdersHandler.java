package ai.pesco.delivery.core.application.useCase.queries.getNotCompletedOrders;

import java.util.List;

public interface GetNotCompletedOrdersHandler {
    List<GetNotCompletedOrdersResponse> handle();
}
