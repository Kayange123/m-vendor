package dev.kayange.Multivendor.E.commerce.utils;

import dev.kayange.Multivendor.E.commerce.dto.request.ProductRequest;
import dev.kayange.Multivendor.E.commerce.entity.product.Product;

import java.math.BigDecimal;

public abstract class EntityMapper {
    public static Product convertToProduct(ProductRequest request){
        return Product.builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .basePrice(BigDecimal.valueOf(request.getBasePrice()))
                //.coverImage(request.getCoverImage())
                .shortDescription(request.getShortDescription())
                .isShipping(request.getIsShipping())
                .build();
    }
}
