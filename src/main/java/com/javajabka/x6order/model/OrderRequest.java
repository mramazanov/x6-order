package com.javajabka.x6order.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class OrderRequest {
    private final Long userId;
    private final Long productId;
    private final Long quantity;
}