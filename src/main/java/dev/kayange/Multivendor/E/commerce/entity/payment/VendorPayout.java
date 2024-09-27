package dev.kayange.Multivendor.E.commerce.entity.payment;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import dev.kayange.Multivendor.E.commerce.entity.users.Vendor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorPayout extends Auditable {

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private double amount;
    private String status;
}
