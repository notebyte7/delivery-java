package ai.pesco.delivery.infrastrucure.adapter.postgres.repository.domain;

import ai.pesco.delivery.core.application.port.repository.UnitOfWorkPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class UnitOfWorkAdapter implements UnitOfWorkPort {
    @Transactional
    @Override
    public void executeInTransaction(Runnable action) {
        action.run();
    }
}
