package com.javajabka.x6order.listener;

import com.javajabka.x6order.configuration.RabbitConfigurationProperties;
import com.javajabka.x6order.model.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitConfigurationProperties properties;

    public void send(OrderResponse orderResponse) {
        rabbitTemplate.convertAndSend(properties.getExchange(), properties.getQueue(), orderResponse);
    }
}