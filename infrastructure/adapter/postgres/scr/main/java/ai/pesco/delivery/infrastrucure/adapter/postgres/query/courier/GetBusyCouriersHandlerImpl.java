package ai.pesco.delivery.infrastrucure.adapter.postgres.query.courier;

import ai.pesco.delivery.core.application.useCase.queries.getBusyCouriers.GetBusyCouriersHandler;
import ai.pesco.delivery.core.application.useCase.queries.getBusyCouriers.GetBusyCouriersResponse;
import ai.pesco.delivery.core.domain.model.courierAggregate.CourierStatus;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import ai.pesco.delivery.infrastrucure.adapter.postgres.model.entity.CourierJpaEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.UUID;

public class GetBusyCouriersHandlerImpl implements GetBusyCouriersHandler {
    private static final String SQL = String.format("SELECT * FROM %s WHERE status = :status", CourierJpaEntity.TABLE_NAME);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GetBusyCouriersHandlerImpl(NamedParameterJdbcTemplate jdbcTemplate, Location location) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GetBusyCouriersResponse> handle() {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("status", CourierStatus.BUSY.name());
        return jdbcTemplate.query(SQL, paramMap, mapRowToResponseDto());
    }

    private RowMapper<GetBusyCouriersResponse> mapRowToResponseDto() {
        return (rs, rowNum) -> {
            Location location;
            return new GetBusyCouriersResponse(
                    (UUID) rs.getObject("id"),
                    rs.getString("name"),
                    new GetBusyCouriersResponse.Location(location.getX(), location.getY()),
                    (UUID) rs.getObject("transport_id")
            );
        };
    }
}
