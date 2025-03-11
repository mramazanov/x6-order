package com.javajabka.x6order.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private final Long id;
    private final String name;
    private final String email;
}