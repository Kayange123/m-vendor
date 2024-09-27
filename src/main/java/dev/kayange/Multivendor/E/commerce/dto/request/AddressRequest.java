package dev.kayange.Multivendor.E.commerce.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record AddressRequest(
       @NotEmpty(message = "Street Value should not be empty") String street,
       @NotEmpty(message = "City name should not be empty") String city,
       @NotEmpty(message = "Postal code should not be empty") String postalCode,
       @NotEmpty(message = "Country Name should be provided") String country
) {
}
