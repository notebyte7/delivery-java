package ai.pesco.delivery.infrastrucure.adapter.postgres.model.entity;

import ai.pesco.delivery.core.domain.model.orderAggregate.Order;
import ai.pesco.delivery.core.domain.model.orderAggregate.OrderStatus;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = OrderJpaEntity.TABLE_NAME)
public class OrderJpaEntity extends JpaEntity<Order> {
    public static final String TABLE_NAME = "orders";

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(columnDefinition = "order_status")
    private OrderStatus status;

    private Location location;

    private UUID courierId;

    @Override
    public Class<Order> getDomainAggregateClass() {
        return Order.class;
    }

    public static OrderJpaEntity fromDomain(Order domain) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setId(domain.getId());
        entity.setStatus(domain.getStatus());
        entity.setLocation(domain.getLocation());
        entity.setCourierId(domain.getCourierId());
        return entity;
    }
}
