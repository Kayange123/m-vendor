package dev.kayange.Multivendor.E.commerce.api;

import dev.kayange.Multivendor.E.commerce.dto.CartResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.ApiResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.CartItemRequest;
import dev.kayange.Multivendor.E.commerce.entity.cart.Cart;
import dev.kayange.Multivendor.E.commerce.security.CurrentUser;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import dev.kayange.Multivendor.E.commerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("carts")
@Tag(name = "Carts", description = "The list of carts related API")
public class CartController {
    private final CartService cartService;

    @PostMapping("/user-cart")
    @Operation(summary = "Get logged in user's cart")
    public ResponseEntity<?> getCustomerCart(@CurrentUser UserPrincipal user){
        Cart cart = cartService.getCustomerCart(user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Cart retrieved successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .data(CartResponse.create(cart))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/user-cart/add-to-cart")
    @Operation(summary = "Add items to user's cart")
    public ResponseEntity<?> addToCart(@RequestBody List<CartItemRequest> items, @CurrentUser UserPrincipal user){
        cartService.addItemsToCart(items, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Items added successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/user-cart/remove-item/{itemId}")
    @Operation(summary = "Add items to user's cart")
    public ResponseEntity<?> removeFromCart(@PathVariable("itemId") Long id, @CurrentUser UserPrincipal user){
        cartService.removeItemFromCart(id, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Item removed successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
