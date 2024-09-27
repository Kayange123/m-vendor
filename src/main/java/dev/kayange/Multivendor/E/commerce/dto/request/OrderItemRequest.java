package dev.kayange.Multivendor.E.commerce.dto.request;

public record OrderItemRequest(
        int quantity,
        double price,
        long productId
){}
