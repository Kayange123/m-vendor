package dev.kayange.Multivendor.E.commerce.dto.request;

import java.util.List;


public record CartRequest (List<CartItemRequest> cartItems) {
}
