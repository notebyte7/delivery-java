package ai.pesco.delivery.infrastrucure.adapter.postgres.repository.domain;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ai.pesco.delivery.core.application.port.repository.domain.OrderRepositoryPort;
import ai.pesco.delivery.core.domain.model.orderAggregate.Order;
import ai.pesco.delivery.core.domain.model.orderAggregate.OrderStatus;
import ai.pesco.delivery.infrastrucure.adapter.postgres.model.entity.OrderJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostgresOrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository jpaRepository;

    @Override
    public Order findAnyWithCreatedStatus() {
        return jpaRepository.findTop1ByStatus(OrderStatus.CREATED)
                .map(OrderJpaEntity::toDomain)
                .orElse(null);
    }

    @Override
    public Collection<Order> findAllWithAssignedStatus() {
        return jpaRepository.findAllByStatus(OrderStatus.ASSIGNED).stream()
                .map(OrderJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void add(Order aggregate) {
        OrderJpaEntity jpaEntity = OrderJpaEntity.fromDomain(aggregate);
        jpaRepository.save(jpaEntity);
    }

    @Override
    public void update(Order aggregate) {
        OrderJpaEntity jpaEntity = OrderJpaEntity.fromDomain(aggregate);
        jpaRepository.save(jpaEntity);
    }

    @Override
    public void update(Collection<Order> orders) {
        List<OrderJpaEntity> jpaEntities = orders.stream()
                .map(OrderJpaEntity::fromDomain)
                .collect(Collectors.toList());
        jpaRepository.saveAll(jpaEntities);
    }

    @Override
    public Order findById(UUID id) {
        return jpaRepository.findById(id)
                .map(OrderJpaEntity::toDomain)
                .orElse(null);
    }

    @Override
    public Collection<Order> findAll() {
        return jpaRepository.findAll().stream()
                .map(OrderJpaEntity::toDomain)
                .collect(Collectors.toList());
    }
}
