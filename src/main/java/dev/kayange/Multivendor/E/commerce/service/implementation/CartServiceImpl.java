package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.CartItemRepository;
import dev.kayange.Multivendor.E.commerce.dao.CartRepository;
import dev.kayange.Multivendor.E.commerce.dto.request.CartItemRequest;
import dev.kayange.Multivendor.E.commerce.entity.cart.Cart;
import dev.kayange.Multivendor.E.commerce.entity.cart.CartItem;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.exception.ResourceNotFoundException;
import dev.kayange.Multivendor.E.commerce.service.CartService;
import dev.kayange.Multivendor.E.commerce.service.ProductService;
import dev.kayange.Multivendor.E.commerce.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ProductService productService;

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.saveAndFlush(cart);
    }

    @Override
    public Cart findCartByUserId(String userId) {
        return cartRepository.findCartByCustomerUserId(userId).orElse(null);
    }

    private Cart findCartByUserId(Long userId) {
        return cartRepository.findCartByCustomerId(userId).orElse(null);
    }

    @Override
    public void initiateCart(String userId) {

    }

    @Override
    public Cart getCustomerCart(Long userId) {
        Cart userCart = findCartByUserId(userId);
        if(userCart == null){
           return createCart(userId);
        }
        return userCart;
    }

    @Override
    @Transactional
    public void addItemsToCart(List<CartItemRequest> items, Long userId) {
        var cart = getCustomerCart(userId);
        for (CartItemRequest request : items){
            var product = productService.findProductByPublicId(request.productPublicId());
            var sku = productService.findSkuByProductId(product.getId());
            var exists = getCartItemById(request.itemId());
            if(exists != null){
                exists.setQuantity(request.quantity());
                cartItemRepository.save(exists);
            }else {
                var cartItem = CartItem.builder().quantity(request.quantity())
                        .product(product)
                        .cart(cart)
                        .productSku(sku)
                        .build();
                cartItemRepository.save(cartItem);
            }
        }
    }

    private CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id).orElse(null);
    }

    @Override
    public void removeItemFromCart(Long id, Long userId) {
        var cart = findCartByUserId(userId);
        var item = findCartItemById(id);
        if(!Objects.equals(cart.getId(), item.getCart().getId())) throw new ApiException("This item is not in your cart. You can not delete");
        cartItemRepository.delete(item);
    }

    private CartItem findCartItemById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Cart item not found"));
    }

    private Cart createCart(Long userId) {
        var customer = userService.findUserById(userId);
        Cart cart = Cart.builder()
                .customer(customer)
                .cartItems(List.of())
                .build();
        return saveCart(cart);
    }
}
