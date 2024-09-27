package dev.kayange.Multivendor.E.commerce.entity.users;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "vendors")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vendor extends Auditable {
    private String name;
    private String phoneNumber;
    private String bio;
    private boolean suspended = false;
    private boolean activated = false;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;  // Link back to Customer entity

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
