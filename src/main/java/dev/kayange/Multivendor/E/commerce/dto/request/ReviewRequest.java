package dev.kayange.Multivendor.E.commerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ReviewRequest(
        @NotEmpty(message = "Reply message should not be empty")  String content,
       @NotNull Double rating,
       @NotNull Long productId
) { }
