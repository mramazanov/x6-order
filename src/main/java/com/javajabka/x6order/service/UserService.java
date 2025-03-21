package com.javajabka.x6order.service;

import com.javajabka.x6order.model.OrderRequest;
import com.javajabka.x6order.model.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;

    public UserResponse checkUser(OrderRequest orderRequest) {
        return restTemplate.getForObject("http://localhost:8081/api/v1/user/{id}", UserResponse.class, orderRequest.getUserId());
    }
}