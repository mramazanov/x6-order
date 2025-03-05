package com.javajabka.x6order.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@RequiredArgsConstructor
public class OrderResponse implements Serializable {
    private final Long id;
    private final Long userId;
    private final Long productId;
    private final Long quantity;
}