package com.javajabka.x6order;

import com.javajabka.x6order.exception.BadRequestException;
import com.javajabka.x6order.listener.NotificationProducer;
import com.javajabka.x6order.model.OrderRequest;
import com.javajabka.x6order.model.OrderResponse;
import com.javajabka.x6order.model.ProductQuantity;
import com.javajabka.x6order.model.UserResponse;
import com.javajabka.x6order.repository.OrderRepository;
import com.javajabka.x6order.service.OrderService;
import com.javajabka.x6order.service.ProductService;
import com.javajabka.x6order.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private NotificationProducer notificationProducer;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void createOrder_valid() {
        ProductQuantity productQuantity_one = ProductQuantity.builder()
                .productId(1L)
                .quantity(100L)
                .build();

        ProductQuantity productQuantity_two = ProductQuantity.builder()
                .productId(2L)
                .quantity(100L)
                .build();

        ProductQuantity productQuantity_three = ProductQuantity.builder()
                .productId(3L)
                .quantity(100L)
                .build();

        OrderRequest orderRequest = buildOrderRequest(1L, Arrays.asList(productQuantity_one, productQuantity_two, productQuantity_three));
        OrderResponse orderResponse = buildOrderResponse(1L, orderRequest);
        Mockito.when(productService.checkProducts(orderRequest)).thenReturn(null);
        Mockito.when(userService.checkUser(orderRequest)).thenReturn(Mockito.mock(UserResponse.class));
        Mockito.when(orderRepository.createOrder(orderRequest)).thenReturn(orderResponse);
        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        Assertions.assertEquals(createdOrder.getId(), orderResponse.getId());
        Mockito.verify(orderRepository).createOrder(orderRequest);
    }

    @Test
    public void shouldReturnError_whenUserIdIsNull() {
        ProductQuantity productQuantity_one = ProductQuantity.builder()
                .productId(null)
                .quantity(100L)
                .build();

        OrderRequest orderRequest = buildOrderRequest(null, List.of(productQuantity_one));

        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class, () -> orderService.createOrder(orderRequest)
        );
        Assertions.assertEquals("Введите id пользователя", exception.getMessage());
    }

    @Test
    public void shouldReturnError_whenUserIdIsNotExist() {
        ProductQuantity productQuantity_one = ProductQuantity.builder()
                .productId(1L)
                .quantity(100L)
                .build();
        OrderRequest orderRequest = OrderRequest.builder()
                .userId(100L)
                .products(List.of(productQuantity_one))
                .build();

        Mockito.when(userService.checkUser(orderRequest)).thenThrow(new BadRequestException("Не удалось найти пользователя с id = 100"));
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class, () -> orderService.createOrder(orderRequest)
        );
        Assertions.assertEquals("Не удалось найти пользователя с id = 100", exception.getMessage());
    }

    private OrderResponse buildOrderResponse(final Long userId, final OrderRequest orderRequest) {
        return OrderResponse.builder()
                .id(userId)
                .userId(orderRequest.getUserId())
                .products(orderRequest.getProducts())
                .build();
    }

    private OrderRequest buildOrderRequest(final Long userId, final List<ProductQuantity> productIds) {
        return OrderRequest.builder()
                .userId(userId)
                .products(productIds)
                .build();
    }
}