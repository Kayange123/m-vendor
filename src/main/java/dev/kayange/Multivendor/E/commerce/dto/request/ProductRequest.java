package dev.kayange.Multivendor.E.commerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ProductRequest {
    @NotEmpty(message = "Product Name should not be empty")
    @Size(max = 100, min = 5, message = "Product Name should be between 5 and 100 characters")
    private String productName;
    private Long categoryId;
    private Long subCategoryId;
    @NotEmpty(message = "Product Name should not be empty")
    @Size(max = 1000, min = 20, message = "Product short description should be between 20 and 1000 characters")
    private String shortDescription;
    @NotEmpty(message = "Product long description should not be empty")
    @Size(max = 10000, min = 20, message = "Product Name should be between 20 and 10000 characters")
    private String productDescription;
    @PositiveOrZero(message = "Product Price should be Given")
    private double basePrice;
    private Boolean isShipping;
    private Integer numberOfProducts;
    private List<Attribute> attributes;

}

