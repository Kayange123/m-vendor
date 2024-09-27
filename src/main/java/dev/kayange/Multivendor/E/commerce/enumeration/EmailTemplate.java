package dev.kayange.Multivendor.E.commerce.enumeration;

import lombok.Getter;

@Getter
public enum EmailTemplate {
    ACTIVATE_ACCOUNT("activate_account"),
    ALERTS("alerts"),
    RESET_PASSWORD("reset_password");

    private final String templateName;

    EmailTemplate(String templateName) {
        this.templateName = templateName;
    }
}
