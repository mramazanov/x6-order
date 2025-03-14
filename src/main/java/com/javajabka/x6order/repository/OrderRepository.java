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
            WITH createOrder AS (
                INSERT INTO x6order.order (userId, create_date)
                VALUES (:userId, now())
                RETURNING *
            ), createOrderProducts AS (
                INSERT INTO x6order.products (order_id, product, quantity)
                (SELECT createOrder.id, unnest(:productIds), :quantity FROM createOrder)
                RETURNING *
            )
            
            SELECT createOrder.id, createOrder.userid, (SELECT ARRAY(SELECT createOrderProducts.product FROM createOrderProducts)) AS products, createOrderProducts.quantity  FROM createOrder, createOrderProducts limit 1;
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
            params.addValue("productIds", orderRequest.getProducts().toArray(Long[]::new));
            params.addValue("quantity", orderRequest.getQuantity());
        }

        return params;
    }
}