package com.javajabka.x6order.service;

import com.javajabka.x6order.exception.BadRequestException;
import com.javajabka.x6order.model.OrderRequest;
import com.javajabka.x6order.model.OrderResponse;
import com.javajabka.x6order.model.ProductResponse;
import com.javajabka.x6order.model.UserResponse;
import com.javajabka.x6order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Transactional(rollbackFor = Exception.class)
    public OrderResponse createOrder(final OrderRequest orderRequest) {
        validate(orderRequest);

        restTemplate.getForObject("http://localhost:8081/api/v1/user/{id}", UserResponse.class, orderRequest.getUserId());
        restTemplate.getForObject("http://localhost:8082/api/v1/product/{id}", ProductResponse.class, orderRequest.getProductId());

        return orderRepository.createOrder(orderRequest);
    }

    private void validate(OrderRequest orderRequest) {
        if (orderRequest == null) {
            throw new BadRequestException("Введите данные о заказе");
        }
        if (orderRequest.getUserId() == null) {
            throw new BadRequestException("Введите id пользователя");
        }
        if (orderRequest.getProductId() == null) {
            throw new BadRequestException("Введите id продукта");
        }
        if (orderRequest.getQuantity() == null || orderRequest.getQuantity() <= 0) {
            throw new BadRequestException("Введите количество выбранного продукта больше нуля");
        }
    }
}