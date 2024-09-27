package dev.kayange.Multivendor.E.commerce.entity;

import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.enumeration.VendingStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vending_requests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendingRequest extends Auditable{
    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "id")
    private Customer customer;
    @Enumerated(EnumType.STRING)
    private VendingStatus status;
}
