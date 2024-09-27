package dev.kayange.Multivendor.E.commerce.entity.cart;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "carts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cart extends Auditable {

    @OneToOne
    @JoinColumn(name = "user_id")
    private Customer customer;

    @OneToMany(mappedBy = "cart", orphanRemoval = true)
    private List<CartItem> cartItems;

    @Transient
    public int getTotalItems() {
        if(cartItems == null) return 0;
        return cartItems.size();
    }

    @Transient
    public int getTotalPrice() {
        if(cartItems == null || cartItems.size()== 0) return 0;
        return cartItems.stream().mapToInt(cart-> cart.getQuantity() * cart.getProductSku().getPrice().intValue()).sum();
    }
}
