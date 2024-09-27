package dev.kayange.Multivendor.E.commerce.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CategoryRequest(
        @NotEmpty(message = "CategoryName should be provided") String categoryName,
        @NotEmpty(message = "Category Description must be provided") String categoryDescription,
        @NotEmpty(message = "Category icon should be provided") String categoryIcon
        ) {
}
