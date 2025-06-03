package ai.pesco.delivery.api.adapter.scheduler.courier;

import ai.pesco.delivery.core.application.useCase.commands.moveCouriers.MoveCouriersHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourierScheduler {

    private final MoveCouriersHandler moveCouriersHandler;

    @Scheduled(fixedDelay = 1000)
    private void moveCouriers() {
        log.info("Launching the courier movement planner.");
        moveCouriersHandler.handle();
    }
}