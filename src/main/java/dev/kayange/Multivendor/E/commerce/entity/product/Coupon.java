package dev.kayange.Multivendor.E.commerce.entity.product;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "coupons")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Coupon extends Auditable {
    private String code;
    private String couponDescription;
    private BigDecimal discountValue;
    private String discountType;
    private Integer timesUsed;
    private Integer maxUsage;
    private LocalDateTime couponStartDate;
    private LocalDateTime couponEndDate;

    @ManyToMany(mappedBy = "coupons")
    private Set<Product> products;

    /*@OneToMany(mappedBy = "coupon")
    private List<Order> orders;*/
}
