package dev.kayange.Multivendor.E.commerce.service;

import com.stripe.model.Charge;

public interface PaymentService {
    Charge chargeCreditCard(String stripeToken, double amount, String currency);

    void processPayment(String paymentToken, double totalAmount, String currency);
}
