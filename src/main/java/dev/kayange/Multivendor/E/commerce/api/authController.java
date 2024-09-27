package dev.kayange.Multivendor.E.commerce.api;

import dev.kayange.Multivendor.E.commerce.dto.PasswordResetRequest;
import dev.kayange.Multivendor.E.commerce.dto.VerificationReguest;
import dev.kayange.Multivendor.E.commerce.dto.request.ApiResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.LoginCredentials;
import dev.kayange.Multivendor.E.commerce.dto.request.RegistrationRequest;
import dev.kayange.Multivendor.E.commerce.dto.response.AuthResponse;
import dev.kayange.Multivendor.E.commerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("auth")
@Tag(name = "Authentication", description = "The authentication related APIs")
public class authController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(method = "POST", summary = "Register a new user")
    public ResponseEntity<ApiResponse<?>> register(
            @Valid @RequestBody RegistrationRequest registrationRequest) throws MessagingException {
        authService.register(registrationRequest);
        var res = ApiResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.name())
                .message("Account Created successfully")
                .build();
        return new ResponseEntity<>(res , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(method = "POST", summary = "Sign In to the system")
    public ResponseEntity<ApiResponse<?>> login(
            @Valid @RequestBody LoginCredentials credentials) {
        AuthResponse response = authService.authenticate(credentials);
        var res = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .data(response)
                .status(HttpStatus.OK.name())
                .message("Login successful")
                .build();
        return new ResponseEntity<>(res , HttpStatus.OK);
    }

    @PostMapping("/account/re-verify")
    @Operation(method = "POST", summary = "Request another Account verification code")
    public ResponseEntity<ApiResponse<?>> reVerifyAccount(
            @Valid @RequestBody VerificationReguest verificationReguest) {
        authService.reVerifyAccount(verificationReguest);
        var res = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Verification Request sent successfully. Check your email")
                .build();
        return new ResponseEntity<>(res , HttpStatus.OK);
    }

    @PostMapping("/ps/sefpr")
    @Operation(method = "POST", summary = "Send Email For Password Request")
    public ResponseEntity<ApiResponse<?>> PasswordResetRequest(
            @Valid @RequestBody VerificationReguest verificationReguest) {
        authService.resetPasswordRequest(verificationReguest);
        var res = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Password Request Link was sent successfully. Check your email")
                .build();
        return new ResponseEntity<>(res , HttpStatus.OK);
    }

    @PostMapping("/ps/reset-password")
    @Operation(method = "POST", summary = "Reset Password")
    public ResponseEntity<ApiResponse<?>> resetPassword(
            @Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        authService.resetPassword(passwordResetRequest);
        var res = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .message("Password was reset successfully")
                .build();
        return new ResponseEntity<>(res , HttpStatus.OK);
    }

    @GetMapping("/logout")
    @Operation(method = "POST", summary = "Logout handler - Optional")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            //String username = auth.getName();
            // TODO: 01/09/2024 Clear OTP details from the cache
            //otpService.clearOTP(username);
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

    @PostMapping("/activate-account")
    @Operation(method = "POST", summary = "Activate An Account")
    public ResponseEntity<?> activateAccount(@RequestParam("token") String token) throws MessagingException {
        authService.activateAccount(token);
        var response = ApiResponse.builder()
                .status(HttpStatus.OK.toString())
                .statusCode(HttpStatus.OK.value())
                .message("Token Verified Successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
