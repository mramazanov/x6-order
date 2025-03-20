package com.javajabka.x6order.repository.mapper;

import com.javajabka.x6order.model.ProductQuantity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductQuantityMapper implements RowMapper<ProductQuantity> {
    @Override
    public ProductQuantity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ProductQuantity.builder()
                .productId(rs.getLong("product_id"))
                .quantity(rs.getLong("quantity"))
                .build();
    }
}