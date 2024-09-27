package dev.kayange.Multivendor.E.commerce.service.implementation;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import dev.kayange.Multivendor.E.commerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.api.key}")
    private String apiKey;

    @Override
    public Charge chargeCreditCard(String stripeToken, double amount, String currency) {
        Stripe.apiKey = apiKey;
        try{
            return Charge.create(
                    Map.of(
                            "amount", amount, // Amount in cents
                            "currency", currency,
                            "source", stripeToken,
                            "description", "Example of test charge"
                    )
            );
        }catch (Exception e){
            throw new RuntimeException("Payment processing failed", e);
        }
    }

    @Override
    public void processPayment(String paymentToken, double totalAmount, String currency) {

    }
}
