package dev.kayange.Multivendor.E.commerce.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.kayange.Multivendor.E.commerce.entity.*;
import dev.kayange.Multivendor.E.commerce.entity.category.Category;
import dev.kayange.Multivendor.E.commerce.entity.reviews.Review;
import dev.kayange.Multivendor.E.commerce.entity.users.Vendor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product extends Auditable {
    private String productName;
    private String publicId;
    private String shortDescription;
    private String productDescription;
    private BigDecimal basePrice;
    private Boolean publishable;
    private Boolean featured;
    private Boolean onSale;
    private Boolean isTaxable;
    private Boolean isShipping;
    private Boolean promoted;
    private String coverImage;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @ManyToMany
    @JoinTable(
            name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSku> productSkus;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<ImageGallery> galleries;

    @ElementCollection
    @Column(length = 1000)
    @JsonIgnore
    private List<String> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "product_coupons",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    private Set<Coupon> coupons;

    @Transient
    public int getAvailableProducts() {
        if(productSkus.size() == 0) return 0;
        return productSkus.stream().mapToInt(ProductSku::getQuantity).sum();
    }

    @Transient
    public double getAverageRating() {
        return reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
    }
}
