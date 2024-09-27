package dev.kayange.Multivendor.E.commerce.api;

import dev.kayange.Multivendor.E.commerce.dto.PageResponse;
import dev.kayange.Multivendor.E.commerce.dto.ProductResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.ApiResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.ProductRequest;
import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import dev.kayange.Multivendor.E.commerce.security.CurrentUser;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import dev.kayange.Multivendor.E.commerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Products", description = "The products component API endpoints")
@RequestMapping("products")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    @Operation(summary = "Create a new product")
    public ResponseEntity<?> createNewProduct(
            @RequestBody @Valid ProductRequest productRequest,
            @CurrentUser UserPrincipal user
    ){
        productService.saveProduct(productRequest, user);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Product created successfully. Waiting for approval before it is made public")
                .status(HttpStatus.CREATED.name())
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Long id,
            @RequestBody @Valid ProductRequest productRequest,
            @CurrentUser UserPrincipal user
    ){
        productService.editProduct(id, productRequest, user);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Product edited successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Find all products")
    public ResponseEntity<?> findProductById(@PathVariable("id") Long id){
        var product = productService.getProductById(id);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Product fetched successfully")
                .status(HttpStatus.OK.name())
                .data(product)
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/verify-product")
    @Operation(summary = "Approve / Verify Product - Only Authorized Users")
    public ResponseEntity<?> activateProduct(
            @PathVariable("id") Long id,
            @CurrentUser UserPrincipal user
    ){
        productService.verifyProduct(id, user);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Product verified successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate / Ban Product - Only Authorized Users")
    public ResponseEntity<?> deactivateProduct(
            @PathVariable("id") Long id,
            @CurrentUser UserPrincipal user
    ){
        productService.deactivateProduct(id, user);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Product deactivated successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    @Operation(summary = "Find all products by pages and sizes", description = "Find all products")
    public ResponseEntity<?> findAllProducts(
            @RequestParam(name = "page", defaultValue = "0", required = false ) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false ) int size,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "query", defaultValue = "_", required = false) String query
    ){
        var products = productService.findAllProducts(sortBy, query, page, size);
        return getResponseEntity(products);
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "Find all products by category pages and sizes", description = "Find all products")
    public ResponseEntity<?> findAllProducts(@PathVariable("id") Long id, @RequestParam(name = "page", defaultValue = "0", required = false ) int page, @RequestParam(name = "size", defaultValue = "10", required = false ) int size){
        var products = productService.findProductsByCategory(id, page, size);
        return getResponseEntity(products);
    }

    @GetMapping("/featured")
    @Operation(summary = "Find all Featured products by pages and sizes", description = "Find all products")
    public ResponseEntity<?> findAllFeaturedProducts(@RequestParam(name = "page", defaultValue = "0", required = false ) int page, @RequestParam(name = "size", defaultValue = "10", required = false ) int size){
        var products = productService.findFeaturedProducts( page, size);
        return getResponseEntity(products);
    }

    @GetMapping("/promoted")
    @Operation(summary = "Find all Promoted products by pages and sizes", description = "Find all products")
    public ResponseEntity<?> findAllPromotedProducts(@RequestParam(name = "page", defaultValue = "0", required = false ) int page, @RequestParam(name = "size", defaultValue = "10", required = false ) int size){
        var products = productService.findAllPromotedProducts( page, size);
        return getResponseEntity(products);
    }

    @GetMapping("/featured-promoted")
    @Operation(summary = "Find all Featured Promoted products by pages and sizes", description = "Find all products")
    public ResponseEntity<?> findAllFeaturedPromotedProducts(@RequestParam(name = "page", defaultValue = "0", required = false ) int page, @RequestParam(name = "size", defaultValue = "10", required = false ) int size){
        var products = productService.findAllFeaturedPromotedProducts( page, size);
        return getResponseEntity(products);
    }

    @GetMapping("/vendor/{publicId}")
    @Operation(summary = "Find all products by vendor", description = "Find all products")
    public ResponseEntity<?> findAllProductsByVendor(
            @PathVariable("publicId") String vendorId,
            @RequestParam(name = "page", defaultValue = "0", required = false ) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false ) int size){
        var products = productService.findProductsByHost(vendorId, page, size);
        return getResponseEntity(products);
    }

    private ResponseEntity<?> getResponseEntity(Page<Product> products) {
        PageResponse<?> pageResponse = PageResponse.builder()
                .data(products.get().map(ProductResponse::create).toList())
                .first(products.isFirst())
                .last(products.isLast())
                .pageNumber(products.getNumber())
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .build();
        ApiResponse<?> response = ApiResponse.builder()
                .message("Products fetched successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .data(pageResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
