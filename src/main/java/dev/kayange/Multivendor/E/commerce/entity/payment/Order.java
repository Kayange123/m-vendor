package dev.kayange.Multivendor.E.commerce.entity.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order extends Auditable {
    private String orderNumber;
    private boolean isShippingFree;
    private BigDecimal taxAmount;

    @Transient
    public double getTotalOrderAmount() {
        return orderItems.stream()
                .mapToDouble(orderItem -> orderItem.getPrice() * orderItem.getQuantity()).sum();
    }

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDateTime orderDate = LocalDateTime.now();

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @JsonIgnore
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

}
