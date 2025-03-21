package com.javajabka.x6order.service;

import com.javajabka.x6order.exception.BadRequestException;
import com.javajabka.x6order.model.OrderRequest;
import com.javajabka.x6order.model.OrderResponse;
import com.javajabka.x6order.model.ProductQuantity;
import com.javajabka.x6order.model.UserResponse;
import com.javajabka.x6order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Transactional(rollbackFor = Exception.class)
    public OrderResponse createOrder(final OrderRequest orderRequest) {
        validate(orderRequest);

        checkUser(orderRequest);
        checkProducts(orderRequest);

        return orderRepository.createOrder(orderRequest);
    }

    private void validate(OrderRequest orderRequest) {
        if (orderRequest == null) {
            throw new BadRequestException("Введите данные о заказе");
        }
        if (orderRequest.getUserId() == null) {
            throw new BadRequestException("Введите id пользователя");
        }
        if (orderRequest.getProducts() == null) {
            throw new BadRequestException("Введите продукты");
        } else {
            for (ProductQuantity productQuantity : orderRequest.getProducts()) {
                if (productQuantity.getProductId() == null || productQuantity.getProductId() <= 0) {
                    throw new BadRequestException("Введите идентификатор продукта больше нуля");
                }
                if (productQuantity.getQuantity() == null || productQuantity.getQuantity() <= 0) {
                    throw new BadRequestException("Введите количество продуктов больше нуля");
                }
            }
        }
    }

    private void checkProducts(OrderRequest orderRequest) {
        String productIds = orderRequest.getProducts().stream().map(ProductQuantity::getProductId).map(Objects::toString).collect(Collectors.joining(","));
        List<Long> products = restTemplate.getForObject("http://localhost:8082/api/v1/product/exists?ids={ids}", List.class, productIds);

        if (products == null || products.size() != orderRequest.getProducts().size()) {
            throw new BadRequestException("Продукты с одним или несколькими id не найдены");
        }
    }

    private void checkUser(OrderRequest orderRequest) {
        restTemplate.getForObject("http://localhost:8081/api/v1/user/{id}", UserResponse.class, orderRequest.getUserId());
    }
}