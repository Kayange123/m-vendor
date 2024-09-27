package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.dto.request.CartItemRequest;
import dev.kayange.Multivendor.E.commerce.entity.cart.Cart;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;

import java.util.List;

public interface CartService {
    Cart saveCart(Cart cart);
    Cart findCartByUserId(String userId);
    void initiateCart(String userId);

    Cart getCustomerCart(Long userId);

    void addItemsToCart(List<CartItemRequest> items, Long userId);

    void removeItemFromCart(Long id, Long userId);
}
