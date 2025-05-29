package ai.pesco.delivery.infrastrucure.adapter.postgres;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "ai.pesco.delivery.infrastructure.adapter.postgres.model.entity")
@EnableJpaRepositories(basePackages = "ai.pesco.delivery.infrastructure.adapter.postgres.repository")
public class PostgresConfiguration {
}
