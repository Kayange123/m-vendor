package dev.kayange.Multivendor.E.commerce.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RoleRequest(@NotEmpty(message = "Role name is required") String name) {
}
