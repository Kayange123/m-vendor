package dev.kayange.Multivendor.E.commerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ReplyRequest(
       @NotNull Long reviewId,
       @NotEmpty(message = "Reply message should not be empty") String content
) {
}
