package dev.kayange.Multivendor.E.commerce.dto;

import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
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
    private int availableProducts;
    private UserSummary owner;
    private Long categoryId;
    private String categoryName;
    private double rating;

    public static ProductResponse create(Product product){
        var vendor = product.getVendor().getCustomer();
        var owner = new UserSummary(vendor.getUsername(), vendor.getFullName(), vendor.getEmail(), vendor.getProfileImg(), vendor.getUserId());
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .publicId(product.getPublicId())
                .shortDescription(product.getShortDescription())
                .productDescription(product.getProductDescription())
                .basePrice(product.getBasePrice())
                .publishable(product.getPublishable())
                .featured(product.getFeatured())
                .onSale(product.getOnSale())
                .isTaxable(product.getIsTaxable())
                .isShipping(product.getIsShipping())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getCategoryName())
                .promoted(product.getPromoted())
                .coverImage(product.getCoverImage())
                .availableProducts(product.getAvailableProducts())
                .rating(product.getAverageRating())
                .owner(owner)
                .build();
    }

}
