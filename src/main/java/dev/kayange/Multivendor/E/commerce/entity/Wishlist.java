package dev.kayange.Multivendor.E.commerce.entity;

import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "wishlists")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Wishlist extends Auditable {
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
