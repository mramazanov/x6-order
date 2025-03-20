package com.javajabka.x6order.controller;

import com.javajabka.x6order.model.OrderRequest;
import com.javajabka.x6order.model.OrderResponse;
import com.javajabka.x6order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Tag(name = "Заказ")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Создать заказ")
    public OrderResponse createOrder(@RequestBody final OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }
}