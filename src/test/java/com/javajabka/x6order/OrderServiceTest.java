package com.javajabka.x6order;

import com.javajabka.x6order.exception.BadRequestException;
import com.javajabka.x6order.model.OrderRequest;
import com.javajabka.x6order.model.OrderResponse;
import com.javajabka.x6order.model.UserResponse;
import com.javajabka.x6order.repository.OrderRepository;
import com.javajabka.x6order.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void createOrder_valid() {
        OrderRequest orderRequest = buildOrderRequest(1L, 1L, 10l);
        OrderResponse orderResponse = buildOrderResponse(1L, orderRequest);
        Mockito.when(orderRepository.createOrder(orderRequest)).thenReturn(orderResponse);
        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        Assertions.assertEquals(createdOrder.getId(), orderResponse.getId());
        Mockito.verify(orderRepository).createOrder(orderRequest);
    }

    @Test
    public void shouldReturnError_whenUserIdIsNull() {
        OrderRequest orderRequest = buildOrderRequest(null, 1L, 10L);

        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class, () -> orderService.createOrder(orderRequest)
        );
        Assertions.assertEquals("Введите id пользователя", exception.getMessage());
    }

    @Test
    public void shouldReturnError_whenUserIdIsNotExist() {
        Mockito.when(
                restTemplate.getForObject("http://localhost:8081/api/v1/user/{id}", UserResponse.class, 100L)
                ).thenThrow(
                        new BadRequestException("Не удалось найти пользователя с id = 100")
        );
        OrderRequest orderRequest = buildOrderRequest(100L, 1L, 10L);
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class, () -> orderService.createOrder(orderRequest)
        );
        Assertions.assertEquals("Не удалось найти пользователя с id = 100", exception.getMessage());
    }

    private OrderResponse buildOrderResponse(final Long userId, final OrderRequest orderRequest) {
        return OrderResponse.builder()
                .id(userId)
                .userId(orderRequest.getUserId())
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .build();
    }

    private OrderRequest buildOrderRequest(final Long userId, final Long productId, final Long amount) {
        return OrderRequest.builder()
                .userId(userId)
                .productId(productId)
                .quantity(amount)
                .build();
    }
}