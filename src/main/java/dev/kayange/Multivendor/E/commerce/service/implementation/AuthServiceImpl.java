package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.TokenRepository;
import dev.kayange.Multivendor.E.commerce.dto.PasswordResetRequest;
import dev.kayange.Multivendor.E.commerce.dto.VerificationReguest;
import dev.kayange.Multivendor.E.commerce.dto.request.LoginCredentials;
import dev.kayange.Multivendor.E.commerce.dto.request.RegistrationRequest;
import dev.kayange.Multivendor.E.commerce.dto.response.AuthResponse;
import dev.kayange.Multivendor.E.commerce.entity.Token;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.entity.users.Role;
import dev.kayange.Multivendor.E.commerce.entity.users.UserRole;
import dev.kayange.Multivendor.E.commerce.enumeration.EventType;
import dev.kayange.Multivendor.E.commerce.events.UserEvent;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.exception.NotAnException;
import dev.kayange.Multivendor.E.commerce.exception.ResourceNotFoundException;
import dev.kayange.Multivendor.E.commerce.security.JwtTokenProvider;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import dev.kayange.Multivendor.E.commerce.service.AuthService;
import dev.kayange.Multivendor.E.commerce.service.RoleService;
import dev.kayange.Multivendor.E.commerce.service.UserRoleService;
import dev.kayange.Multivendor.E.commerce.service.UserService;
import dev.kayange.Multivendor.E.commerce.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static dev.kayange.Multivendor.E.commerce.constants.AppConstants.DEFAULT_AVATAR;

@Service
@Repository
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final TokenRepository tokenRepository;
    private final ApplicationEventPublisher publisher;

    @Value("${application.mailing.front-end.url}")
    private String confirmationUrl;

    @Value("${application.urls.password-reset}")
    private String passwordResetUrl;

    @Override
    @Transactional
    public void register(RegistrationRequest registration) {
        if(userService.userExistsByEmail(registration.getEmail())){
            throw new ApiException("User with email " + registration.getEmail() + " already registered");
        }
        Role role = roleService.findFirstByName("CUSTOMER");

        var newUser = Customer.builder()
                .firstName(registration.getFirstName())
                .lastName(registration.getLastName())
                .email(registration.getEmail().toLowerCase())
                .password(passwordEncoder.encode(registration.getPassword()))
                .userId(Utils.generateUserId(registration.getLastName()))
                .profileImg(DEFAULT_AVATAR)
                .active(false)
                .addresses(List.of())
                .enabled(false)
                .accountNonExpired(true)
                .locked(false)
                .username(generateUniqueUsername(registration.getFirstName() + " " + registration.getLastName()))
                .build();
        Customer customer = userService.save(newUser);

        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(customer);
        userRoleService.save(userRole);
        String token = generateAndSaveToken(customer);
        publisher.publishEvent(new UserEvent(customer, EventType.REGISTRATION, Map.of("subject", "Confirm your Email", "confirmationUrl", confirmationUrl, "activationCode", token)));
    }

    private String generateUniqueUsername(String s) {
        String username = s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        String uniqueId = UUID.randomUUID().toString().substring(0, 4).toLowerCase();
        return username + uniqueId + "_";
    }

    @Override
    public AuthResponse authenticate(LoginCredentials loginCredentials) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginCredentials.getEmail().toLowerCase(),
                        loginCredentials.getPassword()
                )
        );
        var principal = (UserPrincipal) auth.getPrincipal();
        String token = jwtTokenProvider.generateToken(principal);
        return AuthResponse.builder().accessToken(token).build();
    }

    @Override
    public void activateAccount(String token) {
        Token savedToken = tokenRepository.findTokenByToken(token).orElseThrow(() -> new ApiException("Could not find the token"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            var newToken = generateAndSaveToken(savedToken.getUser());
            publisher.publishEvent(new UserEvent(savedToken.getUser(), EventType.REGISTRATION, Map.of("subject", "Confirm your Email", "confirmationUrl", confirmationUrl, "activationCode", newToken)));
            tokenRepository.delete(savedToken);
            throw new ApiException("Token already expired. New token was sent to your email");
        }
        Customer user = userService.findUserById(savedToken.getUser().getId());
        user.setEnabled(true);
        user.setActive(true);
        userService.save(user);
        tokenRepository.delete(savedToken);
    }

    @Override
    public void resetPasswordRequest(VerificationReguest verificationReguest) {
        Customer user = userService.findByUserEmail(verificationReguest.email());
        var token = generateAndSaveResetPasswordToken(user);
        sendPasswordResetLink(user, token);
    }

    private void sendPasswordResetLink(Customer user, String token) {
        String url = passwordResetUrl + "?token=" + token;
        var properties = Map.of(
                "subject", "Password Reset Request",
                "confirmationUrl", url
        );
        publisher.publishEvent(new UserEvent(user, EventType.PASSWORD_RESET, properties));
    }

    @Override
    public void reVerifyAccount(VerificationReguest verificationReguest) {
        Customer user = userService.findByUserEmail(verificationReguest.email());
        if(user.isEnabled()) throw new NotAnException("User is already enabled");
        var token = findTokenByUserId(user.getId());
        String newToken;
        if(token.isPresent()){
            var availableToken = token.get();
            if(LocalDateTime.now().isAfter(availableToken.getExpiresAt())){
                newToken = generateAndSaveToken(user);
            }else{
                newToken = availableToken.getToken();
            }
        }else{
            newToken = generateAndSaveToken(user);
        }
        publisher.publishEvent(new UserEvent(user, EventType.REGISTRATION, Map.of("subject", "Confirm your Email", "confirmationUrl", confirmationUrl, "activationCode", newToken)));
    }

    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        var token = findTokenByToken(passwordResetRequest.token());
        if(LocalDateTime.now().isAfter(token.getExpiresAt())){
            sendPasswordResetLink(token.getUser(), token.getToken());
            throw new ApiException("Verification link is expired. New link was sent to your email");
        }
        var user = token.getUser();
        user.setPassword(passwordEncoder.encode(passwordResetRequest.password()));
        userService.save(user);
        tokenRepository.delete(token);
    }

    private Optional<Token> findTokenByUserId(Long id) {
        return tokenRepository.findTokenByUserId(id);
    }

    private Token findTokenByToken(String token) {
        return tokenRepository.findTokenByToken(token).orElseThrow(
                ()-> new ResourceNotFoundException("Token " + token + " not found"));
    }

    private String generateAndSaveToken(Customer user){
        var otp = Utils.generateActivationOtp(6);
        var token = Token.builder()
                .token(otp)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        Token saved = tokenRepository.save(token);
        log.info(saved.getToken());
        return otp;
    }

    private String generateAndSaveResetPasswordToken(Customer user){
        var resetToken = UUID.randomUUID().toString();
        var token = Token.builder()
                .token(resetToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(2))
                .user(user)
                .build();
        Token saved = tokenRepository.save(token);
        return saved.getToken();
    }

}
