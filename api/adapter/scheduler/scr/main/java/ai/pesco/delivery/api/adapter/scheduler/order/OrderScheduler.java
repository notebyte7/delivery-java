package ai.pesco.delivery.api.adapter.scheduler.order;

import ai.pesco.delivery.core.application.useCase.commands.assignOrders.AssignOrdersHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final AssignOrdersHandler assignOrdersHandler;

    @Scheduled(fixedDelay = 2000)
    public void assignCouriers() {
        log.info("Launching the appointment planner for orders.");
        assignOrdersHandler.handle();
    }
}
