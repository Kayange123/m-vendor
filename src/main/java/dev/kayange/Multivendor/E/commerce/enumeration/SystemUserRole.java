package dev.kayange.Multivendor.E.commerce.enumeration;

import lombok.Getter;

@Getter
public enum SystemUserRole {
    CUSTOMER("CUSTOMER"),
    VENDOR("VENDOR"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER_ADMIN"),
    MANAGER("MANAGER");

    private final String roleName;

    SystemUserRole(String roleName) {
        this.roleName = roleName;
    }
}
