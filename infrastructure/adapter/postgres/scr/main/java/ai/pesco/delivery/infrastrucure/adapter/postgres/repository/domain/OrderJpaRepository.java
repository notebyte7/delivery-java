package ai.pesco.delivery.infrastrucure.adapter.postgres.repository.domain;

import ai.pesco.delivery.core.domain.model.orderAggregate.OrderStatus;
import ai.pesco.delivery.infrastrucure.adapter.postgres.model.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
    Optional<OrderJpaEntity> findTop1ByStatus(OrderStatus status);

    List<OrderJpaEntity> findAllByStatus(OrderStatus status);
}
