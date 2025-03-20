package com.javajabka.x6order.repository;

import com.javajabka.x6order.model.OrderRequest;
import com.javajabka.x6order.model.OrderResponse;
import com.javajabka.x6order.repository.mapper.OrderMapper;
import com.javajabka.x6order.repository.mapper.ProductQuantityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private static final String INSERT_INTO_ORDER = """
            INSERT INTO x6order.order (userId, create_date)
            VALUES (:userId, now())
            RETURNING *
            """;

    private static final String INSERT_INTO_PRODUCT_QUANTITY = """
            INSERT INTO x6order.products (order_id, product_id, quantity)
            VALUES (:orderId, :productId, :quantity)
            RETURNING *;
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final OrderMapper orderMapper;

    public OrderResponse createOrder(final OrderRequest orderRequest) {
        OrderResponse orderResponse = jdbcTemplate.queryForObject(INSERT_INTO_ORDER, orderToSql(orderRequest), orderMapper);
        jdbcTemplate.batchUpdate(INSERT_INTO_PRODUCT_QUANTITY, productQuntityParams(orderResponse.getId(), orderRequest));

        return OrderResponse.builder()
                .id(orderResponse.getId())
                .userId(orderResponse.getUserId())
                .products(orderRequest.getProducts())
                .build();
    }

    private MapSqlParameterSource orderToSql(final OrderRequest orderRequest) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (orderRequest != null) {
            params.addValue("userId", orderRequest.getUserId());
        }

        return params;
    }

    private MapSqlParameterSource productQuantityToSql(final Long orderId, final Long productId, final Long productQuantity) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("orderId", orderId);
        params.addValue("productId", productId);
        params.addValue("quantity", productQuantity);

        return params;
    }

    private MapSqlParameterSource[] productQuntityParams(final Long orderId, final OrderRequest orderRequest) {
        MapSqlParameterSource[] params = new MapSqlParameterSource[orderRequest.getProducts().size()];

        for (int i = 0; i < orderRequest.getProducts().size(); i++) {
            params[i] = productQuantityToSql(orderId, orderRequest.getProducts().get(i).getProductId(), orderRequest.getProducts().get(i).getQuantity());
        }

        return params;
    }
}