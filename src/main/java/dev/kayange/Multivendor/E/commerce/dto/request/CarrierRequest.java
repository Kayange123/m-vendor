package dev.kayange.Multivendor.E.commerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record CarrierRequest(
        @NotEmpty(message = "Name should not be empty") String name,
        @NotEmpty(message = "Phone Number should Not be empty") String phone,
        @Email(message = "Invalid Email") String email
) {
}
