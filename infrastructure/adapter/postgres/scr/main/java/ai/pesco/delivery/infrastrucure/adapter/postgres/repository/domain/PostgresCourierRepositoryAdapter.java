package ai.pesco.delivery.infrastrucure.adapter.postgres.repository.domain;

import ai.pesco.delivery.core.domain.model.courierAggregate.Courier;
import ai.pesco.delivery.core.domain.model.courierAggregate.CourierStatus;
import ai.pesco.delivery.infrastrucure.adapter.postgres.model.entity.CourierJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PostgresCourierRepositoryAdapter implements CourierRepositoryPort {
    private final CourierJpaRepository jpaRepository;

    @Override
    public Collection<Courier> findAllWithFreeStatus() {
        return jpaRepository.findAllByStatus(CourierStatus.FREE).stream()
                .map(CourierJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void add(Courier aggregate) {
        CourierJpaEntity jpaEntity = CourierJpaEntity.fromDomain(aggregate);
        jpaRepository.save(jpaEntity);
    }

    @Override
    public void update(Courier aggregate) {
        CourierJpaEntity jpaEntity = CourierJpaEntity.fromDomain(aggregate);
        jpaRepository.save(jpaEntity);
    }

    @Override
    public void update(Collection<Courier> couriers) {
        List<CourierJpaEntity> jpaEntities = couriers.stream()
                .map(CourierJpaEntity::fromDomain)
                .collect(Collectors.toList());
        jpaRepository.saveAll(jpaEntities);
    }

    @Override
    public Courier findById(UUID id) {
        return jpaRepository.findById(id)
                .map(CourierJpaEntity::toDomain)
                .orElse(null);
    }

    @Override
    public Collection<Courier> findAll() {
        return jpaRepository.findAll().stream()
                .map(CourierJpaEntity::toDomain)
                .collect(Collectors.toList());
    }
}
