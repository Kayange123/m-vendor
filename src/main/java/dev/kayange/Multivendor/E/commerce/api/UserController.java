package dev.kayange.Multivendor.E.commerce.api;

import dev.kayange.Multivendor.E.commerce.dto.PageResponse;
import dev.kayange.Multivendor.E.commerce.dto.UserProfile;
import dev.kayange.Multivendor.E.commerce.dto.request.ApiResponse;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.security.CurrentUser;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import dev.kayange.Multivendor.E.commerce.service.UserService;
import dev.kayange.Multivendor.E.commerce.utils.AppConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@Tag(name = "Users", description = "Roles associated with system Admins")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/vendor/request")
    @Operation(summary = "Request to become a vendor")
    public ResponseEntity<?> requestToVend(
            @CurrentUser UserPrincipal user
    ){
        userService.requestToBecomeVendor(user);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Vending request was sent successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    @Operation(summary = "Request details of current logged in user")
    public ResponseEntity<?> getUserProfile(
            @CurrentUser UserPrincipal user
    ){
        ApiResponse<?> response = ApiResponse.builder()
                .message("User data retrieved successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .data(UserProfile.create(user))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    @GetMapping("/all-users")
    @Operation(summary = "Retrieve all users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "query", defaultValue = "_", required = false) String query
    ){
        Page<Customer> users = userService.getAllUsers(page, size, sortBy, query);
        var profiles = users.stream().map(UserProfile::create).toList();
        var data = PageResponse.builder().pageNumber(users.getNumber()).data(profiles)
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages()).last(users.isLast())
                .first(users.isFirst()).pageSize(users.getSize()).build();

        ApiResponse<?> response = ApiResponse.builder()
                .message("Users retrieved successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .data(data)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
