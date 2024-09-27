package dev.kayange.Multivendor.E.commerce.dto;

import dev.kayange.Multivendor.E.commerce.entity.cart.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartResponse {
    private int quantity;
    private double totalPrice;

    private List<CartItemResponse> cartItems = new ArrayList<>();

    public static CartResponse create(Cart cart){
        return CartResponse.builder()
                .quantity( cart.getTotalItems())
                .totalPrice(cart.getTotalPrice())
                .cartItems(cart.getCartItems().stream().map(item-> new CartItemResponse(item.getId(), item.getQuantity(), item.getProduct().getPublicId())).toList())
                .build();
    }
}

record CartItemResponse(Long itemId, int quantity, String productPublicId){}

