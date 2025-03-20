package com.javajabka.x6order.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class OrderRequest {
    private final Long userId;
    private final List<ProductQuantity> products;
}