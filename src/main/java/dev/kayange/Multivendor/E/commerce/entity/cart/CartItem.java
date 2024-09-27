package dev.kayange.Multivendor.E.commerce.entity.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import dev.kayange.Multivendor.E.commerce.entity.product.ProductSku;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItem extends Auditable {

        private int quantity;

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "cart_id")
        private Cart cart;

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "product_id")
        private Product product;

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "product_sku_id")
        private ProductSku productSku;


}
