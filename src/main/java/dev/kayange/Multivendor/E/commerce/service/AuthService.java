package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.dto.PasswordResetRequest;
import dev.kayange.Multivendor.E.commerce.dto.VerificationReguest;
import dev.kayange.Multivendor.E.commerce.dto.request.LoginCredentials;
import dev.kayange.Multivendor.E.commerce.dto.request.RegistrationRequest;
import dev.kayange.Multivendor.E.commerce.dto.response.AuthResponse;
import jakarta.mail.MessagingException;

public interface AuthService {
    void register(RegistrationRequest registration) throws MessagingException;

    AuthResponse authenticate(LoginCredentials loginCredentials);

    void activateAccount(String token) throws MessagingException;

    void resetPasswordRequest(VerificationReguest verificationReguest);

    void reVerifyAccount(VerificationReguest verificationReguest);

    void resetPassword(PasswordResetRequest passwordResetRequest);
}
