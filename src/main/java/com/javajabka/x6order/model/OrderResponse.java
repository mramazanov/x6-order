package com.javajabka.x6order.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private final Long id;
    private final Long userId;
    private final List<ProductQuantity> products;
}