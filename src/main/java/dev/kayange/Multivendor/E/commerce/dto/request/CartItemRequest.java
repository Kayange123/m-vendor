package dev.kayange.Multivendor.E.commerce.dto.request;

public record CartItemRequest(Long itemId, int quantity, String productPublicId) {
}
