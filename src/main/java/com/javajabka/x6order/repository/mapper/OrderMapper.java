package com.javajabka.x6order.repository.mapper;

import com.javajabka.x6order.model.OrderResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper implements RowMapper<OrderResponse> {
    @Override
    public OrderResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Long> products = new ArrayList<>();

        ResultSet rs2 = rs.getArray("products").getResultSet();
        while (rs2.next()) {
            products.add(rs2.getLong(1));
        }

        return OrderResponse.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("userid"))
                .products(products)
                .quantity(rs.getLong("quantity"))
                .build();
    }
}