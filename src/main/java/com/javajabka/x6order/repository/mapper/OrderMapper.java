package com.javajabka.x6order.repository.mapper;

import com.javajabka.x6order.model.OrderResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Component
public class OrderMapper implements RowMapper<OrderResponse> {
    @Override
    public OrderResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Long> products = Arrays.asList((Long[]) rs.getArray("products").getArray());
        return OrderResponse.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("userId"))
                .products(products)
                .quantity(rs.getLong("quantity"))
                .build();
    }
}