package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.entity.payment.Order;
import dev.kayange.Multivendor.E.commerce.entity.users.Address;

public interface CheckoutService {
    Order checkout(Long userId, Address shippingAddress, String paymentToken);
}
