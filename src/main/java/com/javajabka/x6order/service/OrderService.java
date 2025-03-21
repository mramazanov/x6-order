package com.javajabka.x6order.service;

import com.javajabka.x6order.exception.BadRequestException;
import com.javajabka.x6order.model.OrderRequest;
import com.javajabka.x6order.model.OrderResponse;
import com.javajabka.x6order.model.ProductQuantity;
import com.javajabka.x6order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final OrderNotificationService notificationService;

    @Transactional(rollbackFor = Exception.class)
    public OrderResponse createOrder(final OrderRequest orderRequest) {
        validate(orderRequest);

        userService.checkUser(orderRequest);
        productService.checkProducts(orderRequest);

        OrderResponse orderResponse = orderRepository.createOrder(orderRequest);
        notificationService.orderNotify(orderResponse);
        return orderResponse;
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


}