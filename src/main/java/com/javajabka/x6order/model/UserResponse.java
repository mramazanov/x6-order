package com.javajabka.x6order.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Builder
public class UserResponse {
    private final Long id;
    private final String name;
    private final String email;
}