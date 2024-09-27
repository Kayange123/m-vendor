package dev.kayange.Multivendor.E.commerce.dto;

import jakarta.validation.constraints.NotEmpty;

public record PasswordResetRequest (
        @NotEmpty(message = "Token should not be empty") String token,
        @NotEmpty(message = "Password should not be empty") String password
        ) {
}
