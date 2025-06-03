package ai.pesco.delivery.infrastrucure.adapter.postgres.query.order;

import ai.pesco.delivery.core.application.useCase.queries.getNotCompletedOrders.GetNotCompletedOrdersHandler;
import ai.pesco.delivery.core.application.useCase.queries.getNotCompletedOrders.GetNotCompletedOrdersResponse;
import ai.pesco.delivery.core.domain.model.orderAggregate.OrderStatus;
import ai.pesco.delivery.core.domain.model.sharedKernel.Location;
import ai.pesco.delivery.infrastrucure.adapter.postgres.model.entity.OrderJpaEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.UUID;

public class GetNotCompletedOrdersHandlerImpl implements GetNotCompletedOrdersHandler {

    private static final String SQL = String.format("SELECT * FROM %s WHERE status != :status", OrderJpaEntity.TABLE_NAME);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GetNotCompletedOrdersHandlerImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GetNotCompletedOrdersResponse> handle() {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("status", OrderStatus.COMPLETED.name());
        return jdbcTemplate.query(SQL, paramMap, mapRowToResponseDto());
    }

    private RowMapper<GetNotCompletedOrdersResponse> mapRowToResponseDto() {
        return (rs, rowNum) -> {
            Location location;
            return new GetNotCompletedOrdersResponse(
                    (UUID) rs.getObject("id"),
                    new GetNotCompletedOrdersResponse.Location(location.getX(), location.getY())
            );
        };
    }
}
