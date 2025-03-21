package com.javajabka.x6order.service;

import com.javajabka.x6order.exception.BadRequestException;
import com.javajabka.x6order.model.OrderRequest;
import com.javajabka.x6order.model.ProductQuantity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final RestTemplate restTemplate;

    public List<Long> checkProducts(OrderRequest orderRequest) {
        String productIds = orderRequest.getProducts().stream().map(ProductQuantity::getProductId).map(Objects::toString).collect(Collectors.joining(","));
        List<Long> products = restTemplate.getForObject("http://localhost:8082/api/v1/product/exists?ids={ids}", List.class, productIds);

        if (products == null || products.size() != orderRequest.getProducts().size()) {
            throw new BadRequestException(String.format("Продукты с id %s не найдены", products));
        }

        return products;
    }
}