package com.javajabka.x6order.repository;

import com.javajabka.x6order.model.OrderRequest;
import com.javajabka.x6order.model.OrderResponse;
import com.javajabka.x6order.repository.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private static final String INSERT = """
            WITH insert_order AS (
                INSERT INTO x6order.order (userId, productId, quantity)
                VALUES (:userId, :productId, :quantity)
                RETURNING *
            ), insert_meta_create_order AS (
                INSERT INTO x6order.meta_order_create (id, create_date)
                (SELECT insert_order.id, now() FROM insert_order)
            )
            SELECT * FROM insert_order;
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final OrderMapper orderMapper;

    public OrderResponse createOrder(final OrderRequest orderRequest) {
        return jdbcTemplate.queryForObject(INSERT, orderToSql(null, orderRequest), orderMapper);
    }

    private MapSqlParameterSource orderToSql(final Long id, final OrderRequest orderRequest) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("userId", id);

        if (orderRequest != null) {
            params.addValue("userId", orderRequest.getUserId());
            params.addValue("productId", orderRequest.getProductId());
            params.addValue("quantity", orderRequest.getQuantity());
        }

        return params;
    }

}
