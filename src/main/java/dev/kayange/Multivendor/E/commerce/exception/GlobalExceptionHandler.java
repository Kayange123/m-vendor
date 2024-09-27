package dev.kayange.Multivendor.E.commerce.exception;

import dev.kayange.Multivendor.E.commerce.dto.request.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@RestControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Triggered if an authentication request is rejected because the account is locked. Makes no
     * assertion whether the credentials were valid.
     *
     * @author Ayubu Kayange
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> lockedExceptionHandler(LockedException exp){
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(
                        ApiResponse.builder()
                                .statusCode(HttpStatus.LOCKED.value())
                                .status(HttpStatus.LOCKED.name())
                                .description("Access Denied: User Account Locked")
                                .error(exp.getMessage())
                                .build()
                );
    }

    /**
     * Triggered if an {@link dev.kayange.Multivendor.E.commerce.security.UserPrincipal Authentication}
     * object does not hold a required authority.
     *
     * @author Ayubu Kayange
     */
    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
    public ResponseEntity<ApiResponse<?>> authenticationExceptionHandler() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ApiResponse.builder()
                                .statusCode(HttpStatus.UNAUTHORIZED.value())
                                .status(HttpStatus.UNAUTHORIZED.name())
                                .error("You are not authenticated")
                                .build()
                );
    }
        /**
         * Triggered if an authentication request is rejected because the account has expired. Makes
         * no assertion whether the credentials were valid.
         *
         * @author Ayubu Kayange
         */
    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<?> accountExpiredExceptionHandler(AccountExpiredException exp){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ApiResponse.builder()
                                .statusCode(HttpStatus.FORBIDDEN.value())
                                .status(HttpStatus.FORBIDDEN.name())
                                .description("Access Denied: Account Expired")
                                .error(exp.getMessage())
                                .build()
                );
    }

    /**
     * Triggered when an exception thrown by the persistence provider when an entity reference obtained by
     * {@link EntityManager#getReference EntityManager.getReference}
     * is accessed but the entity does not exist. Thrown when
     * {@link EntityManager#refresh EntityManager.refresh} is called and the
     * object no longer exists in the database.
     *
     * @author Ayubu Kayange
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundException(EntityNotFoundException exp){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponse.builder()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .status(HttpStatus.BAD_REQUEST.name())
                                .errors("Requested Resource NOT Found")
                                .build()
                );
    }

    /**
     * Triggered if an authentication request is rejected because the account is disabled. Makes
     *  no assertion whether the credentials were valid.
     *
     * @author Ayubu Kayange
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> disabledExceptionHandler(DisabledException exp){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ApiResponse.builder()
                                .status(HttpStatus.FORBIDDEN.name())
                                .statusCode(HttpStatus.FORBIDDEN.value())
                                .error("Access Denied: User Account Disabled")
                                .build()
                );
    }

    /**
     * Triggered if an unknown checked exception has occurred.
     *
     * @author Ayubu Kayange
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException apiException, HttpServletRequest request){
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .error(apiException.getMessage())
                .path(request.getContextPath())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Triggered if an authentication request is rejected because the credentials are invalid.
     * If this handler is triggered, means the account is neither locked nor disabled.
     *
     * @author Ayubu Kayange
     */
    @ExceptionHandler({BadCredentialsException.class})
    protected ResponseEntity<Object> handleBadCredential(
            Exception ex,
            WebRequest request) {

        ApiResponse<?> apiError = ApiResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.name())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .error("Invalid Username or Password")
                .build();

        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Triggered when the {@link dev.kayange.Multivendor.E.commerce.service.MailService MailService}
     * fails to send Email to the destination address
     *
     * @author Ayubu Kayange
     */
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<?> badCredentialsExceptionHandler(MessagingException exp){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiResponse.builder()
                                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                                .error(exp.getMessage())
                                .build()
                );
    }

    /**
     * Triggered when there are validation exceptions
     *
     * @author Ayubu Kayange
     */
    @ExceptionHandler({ValidationException.class})
    protected ResponseEntity<Object> handleValidationException(
            Exception ex,
            WebRequest request) {
        //ex.printStackTrace();

        ApiResponse<?> apiError = ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .error(ex.getMessage())
                .build();

        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Triggered when there are 'unauthorized' requests
     *
     * @author Ayubu Kayange
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestExceptionHandler(BadRequestException exp){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponse.builder()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .status(HttpStatus.BAD_REQUEST.name())
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> expiredJwtExceptionHandler(ExpiredJwtException exp){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ApiResponse.builder()
                                .statusCode(HttpStatus.UNAUTHORIZED.value())
                                .status(HttpStatus.UNAUTHORIZED.name())
                                .error(exp.getMessage())
                                .build()
                );
    }

    /**
     * Triggered if an {@link dev.kayange.Multivendor.E.commerce.security.UserDetailsServiceImpl UserDetailsService} cannot locate a {@link User} by
     * its username.
     *
     * @author Ayubu Kayange
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> usernameNotFoundExceptionHandler(UsernameNotFoundException exp){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.builder()
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .status(HttpStatus.NOT_FOUND.name())
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler({InsufficientAuthenticationException.class})
    protected ResponseEntity<Object> handleAuthenticationRequired(
            InsufficientAuthenticationException ex,
            WebRequest request) {

        ex.printStackTrace();
        List<String> errors = new ArrayList<>();

        ApiResponse<?> apiError = ApiResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.name())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .error("Authentication required")
                .build();

        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class})
    protected ResponseEntity<Object> handleInvalidDataAccessException(
            InvalidDataAccessApiUsageException ex,
            WebRequest request) {
        ex.printStackTrace();

        ApiResponse<?> apiError = ApiResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(ex.getMostSpecificCause().getLocalizedMessage())
                .build();

        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityException(
            DataIntegrityViolationException ex,
            WebRequest request) {
        String error;
        if (ex.getMostSpecificCause().getLocalizedMessage().contains("Detail: Key")) {
            error = StringUtils.substringBetween(
                    ex.getMostSpecificCause().getLocalizedMessage()
                    , "Detail: Key"
                    , ".");
        } else {
            error = StringUtils.substringBetween(
                    ex.getMostSpecificCause().getLocalizedMessage()
                    , "ERROR:"
                    , "of");
        }

        ApiResponse<?> apiError = ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .error(error)
                .build();

        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityException(
            ConstraintViolationException ex,
            WebRequest request) {
        ex.printStackTrace();
        String error;
        if (ex.getMessage().contains("Detail: Key")) {
            error = StringUtils.substringBetween(
                    ex.getLocalizedMessage()
                    , "Detail: Key"
                    , ".");
        } else {
            error = StringUtils.substringBetween(
                    ex.getLocalizedMessage()
                    , "ERROR:"
                    , "of");
        }

        ApiResponse<?> apiError = ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .error(error)
                .build();

        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request){
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .error(exception.getMessage())
                .path(request.getContextPath())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleUncaughtException(Exception e, HttpServletRequest request){
        e.printStackTrace();
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Something went wrong!")
                .path(request.getContextPath())
                .build();
        logger.info(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ActionNotPermittedException.class)
    public ResponseEntity<ApiResponse<?>> actionNotPermittedException(ActionNotPermittedException e, HttpServletRequest request){
        e.printStackTrace();
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .error(e.getMessage())
                .build();
        logger.info(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NotNull MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatusCode status,
            @NotNull WebRequest request) {

         super.handleMethodArgumentNotValid(ex, headers, status, request);

        Set<String> errors = new HashSet<>();
        for (FieldError field : ex.getBindingResult().getFieldErrors()) {
            errors.add((Objects.requireNonNull(field.getDefaultMessage())));
        }

        ApiResponse<?> response = ApiResponse.builder()
                .status(String.valueOf(status.value()))
                .statusCode(status.value())
                .error("Validation Errors")
                .errors(errors)
                .path(request.getContextPath())
                .build();

        return new ResponseEntity<>(response, headers, status);
    }

    @ExceptionHandler(NotAnException.class)
    public ResponseEntity<ApiResponse<?>> handleNotAnException(NotAnException exception){
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .error(exception.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
