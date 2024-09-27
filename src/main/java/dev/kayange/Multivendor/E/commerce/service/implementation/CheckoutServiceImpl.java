package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.CartRepository;
import dev.kayange.Multivendor.E.commerce.dao.OrderRepository;
import dev.kayange.Multivendor.E.commerce.dao.ProductRepository;
import dev.kayange.Multivendor.E.commerce.entity.cart.Cart;
import dev.kayange.Multivendor.E.commerce.entity.cart.CartItem;
import dev.kayange.Multivendor.E.commerce.entity.payment.Order;
import dev.kayange.Multivendor.E.commerce.entity.payment.OrderItem;
import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import dev.kayange.Multivendor.E.commerce.entity.users.Address;
import dev.kayange.Multivendor.E.commerce.enumeration.PaymentStatus;
import dev.kayange.Multivendor.E.commerce.service.CheckoutService;
import dev.kayange.Multivendor.E.commerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;

    @Override
    public Order checkout(Long userId, Address shippingAddress, String paymentToken) {
        Cart cart = findCartByCustomerId(userId);

        Order order = Order.builder()
                .customer(cart.getCustomer())
                //.shippingAddress(shippingAddress)
                .orderDate(LocalDateTime.now())
                //.paymentStatus(PaymentStatus.PENDING)
                .build();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getProductSku().getPrice().doubleValue())
                    .order(order)
                    .build();

            order.getOrderItems().add(orderItem);

            // Update product inventory
            Product product = cartItem.getProduct();

            productRepository.save(product);
        }

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Process payment
        try {
            paymentService.processPayment(paymentToken, calculateTotalAmount(cart), "usd");
        } catch (Exception e) {
            // Handle payment failure (e.g., cancel order, notify user)
            throw new RuntimeException("Payment processing failed", e);
        }

        // Clear shopping cart
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    private double calculateTotalAmount(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(item -> item.getProductSku().getPrice().doubleValue() * item.getQuantity())
                .sum();
    }

    private Cart findCartByCustomerId(Long customerId) {
        return cartRepository.findCartByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }
}
