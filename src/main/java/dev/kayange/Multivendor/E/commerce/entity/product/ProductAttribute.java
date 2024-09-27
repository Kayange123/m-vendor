package dev.kayange.Multivendor.E.commerce.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_attributes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_sku_id")
    private ProductSku productSku;
}
