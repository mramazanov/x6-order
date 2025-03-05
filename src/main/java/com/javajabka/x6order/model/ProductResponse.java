package com.javajabka.x6order.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Data
@Builder
public class ProductResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
}