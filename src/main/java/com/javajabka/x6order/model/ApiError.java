package com.javajabka.x6order.model;

import lombok.Getter;

@Getter
public class ApiError {

    private boolean success;
    private String message;

    public ApiError(final String message) {
        this.success = false;
        this.message = message;
    }
}