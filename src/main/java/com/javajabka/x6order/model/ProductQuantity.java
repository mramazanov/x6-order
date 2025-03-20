package com.javajabka.x6order.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductQuantity {
    private final Long productId;
    private final Long quantity;
}