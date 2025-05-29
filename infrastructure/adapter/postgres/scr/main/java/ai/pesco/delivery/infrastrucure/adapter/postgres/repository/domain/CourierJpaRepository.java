package ai.pesco.delivery.infrastrucure.adapter.postgres.repository.domain;

import ai.pesco.delivery.core.domain.model.courierAggregate.CourierStatus;
import ai.pesco.delivery.infrastrucure.adapter.postgres.model.entity.CourierJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface CourierJpaRepository extends JpaRepository<CourierJpaEntity, UUID> {
    List<CourierJpaEntity> findAllByStatus(CourierStatus status);
}
