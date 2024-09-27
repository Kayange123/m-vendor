package dev.kayange.Multivendor.E.commerce.api;

import dev.kayange.Multivendor.E.commerce.dto.request.ApiResponse;
import dev.kayange.Multivendor.E.commerce.security.CurrentUser;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import dev.kayange.Multivendor.E.commerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@Tag(name = "Administrator", description = "Roles associated with system Admins")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PostMapping("admins/{id}/make-admin")
    @Operation(summary = "Add Role Admin ")
    public ResponseEntity<?> makeAnAdmin(
            @PathVariable("id") Long id,
            @CurrentUser UserPrincipal user
    ){
        userService.makeAnAdmin(id, user);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Admin verified successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/vendor/{id}/verify-account")
    @Operation(summary = "Verify vendor account")
    public ResponseEntity<?> verifyVendorRequest(
            @PathVariable("id") Long id,
            @CurrentUser UserPrincipal user
    ){
        userService.verifyVendorAccount(id, user);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Vendor verified successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
