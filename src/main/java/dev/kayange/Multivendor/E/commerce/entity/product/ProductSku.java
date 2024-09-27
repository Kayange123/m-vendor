package dev.kayange.Multivendor.E.commerce.entity.product;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product_sku")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductSku extends Auditable {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String sku;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer quantity;

    @OneToMany(mappedBy = "productSku", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAttribute> attributes;


}
