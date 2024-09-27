package dev.kayange.Multivendor.E.commerce.entity.reports;

import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products_analytics")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime reportDate;

    private int totalSales;
    private int views;
    private int returns;
}
