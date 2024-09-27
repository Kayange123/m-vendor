package dev.kayange.Multivendor.E.commerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record VerificationReguest(@Email(message = "Email is Invalid") @NotEmpty String email){

}
