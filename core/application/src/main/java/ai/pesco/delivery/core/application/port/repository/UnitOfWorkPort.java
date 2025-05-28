package ai.pesco.delivery.core.application.port.repository;

public interface UnitOfWorkPort {
    void executeInTransaction(Runnable runnable);
}
