package dev.kayange.Multivendor.E.commerce.api;

import dev.kayange.Multivendor.E.commerce.dto.request.AddressRequest;
import dev.kayange.Multivendor.E.commerce.dto.request.ApiResponse;
import dev.kayange.Multivendor.E.commerce.security.CurrentUser;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import dev.kayange.Multivendor.E.commerce.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("address")
@Tag(name = "Address", description = "Manage users Addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/create")
    @Operation(summary = "Create a new Address location")
    public ResponseEntity<?> createAddress(@RequestBody @Valid AddressRequest request, @CurrentUser UserPrincipal user){
        addressService.createAddress(request, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Address created successfully")
                .status(HttpStatus.CREATED.name())
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{addressId}")
    @Operation(summary = "Update Address location")
    public ResponseEntity<?> updateAddress(@RequestBody @Valid AddressRequest request, @CurrentUser UserPrincipal user, @PathVariable("addressId") Long addressId){
        addressService.updateAddress(request, addressId, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Address updated successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{addressId}")
    @Operation(summary = "Update Address location")
    public ResponseEntity<?> deleteAddress(@CurrentUser UserPrincipal user, @PathVariable("addressId") Long addressId){
        addressService.deleteAddress( addressId, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Address was deleted successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
