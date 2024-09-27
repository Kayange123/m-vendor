package dev.kayange.Multivendor.E.commerce.entity.payment;


import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import dev.kayange.Multivendor.E.commerce.enumeration.PaymentMethod;
import dev.kayange.Multivendor.E.commerce.enumeration.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment extends Auditable {

    private String paymentDetails;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String paymentRef;
    private BigDecimal amount;
    private String details;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

}
