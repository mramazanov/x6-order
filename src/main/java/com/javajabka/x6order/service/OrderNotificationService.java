package com.javajabka.x6order.service;

import com.javajabka.x6order.model.NotificationProducer;
import com.javajabka.x6order.model.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderNotificationService {

    private final NotificationProducer notificationProducer;

    public void orderNotify(OrderResponse orderResponse) {
        notificationProducer.send(orderResponse);
    }
}