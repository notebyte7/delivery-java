package ai.pesco.delivery.infrastrucure.adapter.postgres.model.entity;

import ai.pesco.delivery.core.domain.model.courierAggregate.Courier;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = CourierJpaEntity.TABLE_NAME)
public class CourierJpaEntity extends JpaEntity<Courier> {
    public static final String TABLE_NAME = "courier";

    @Id
    private UUID id;

    private String name;

    private Location location;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transport_id", referencedColumnName = "id")
    private TransportJpaEntity transport;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(columnDefinition = "courier_status")
    private CourierStatus status;

    @Override
    public Class<Courier> getDomainAggregateClass() {
        return Courier.class;
    }

    public static CourierJpaEntity fromDomain(Courier domain) {
        CourierJpaEntity entity = new CourierJpaEntity();
        entity.id = domain.getId();
        entity.name = domain.getName();
        entity.location = domain.getLocation();
        entity.transport = TransportJpaEntity.fromDomain(domain.getTransport());
        entity.status = domain.getStatus();
        return entity;
    }
}
