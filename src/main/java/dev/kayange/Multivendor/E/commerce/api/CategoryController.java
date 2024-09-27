package dev.kayange.Multivendor.E.commerce.api;

import dev.kayange.Multivendor.E.commerce.dto.PageResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.ApiResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.CategoryRequest;
import dev.kayange.Multivendor.E.commerce.entity.category.MainCategory;
import dev.kayange.Multivendor.E.commerce.security.CurrentUser;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import dev.kayange.Multivendor.E.commerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/categories")
@Tag(name = "Categories", description = "The categories API endpoints")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(description = "Find All Main categories")
    public ResponseEntity<?> findAll(@RequestParam(name = "page", defaultValue = "0", required = false ) int page, @RequestParam(name = "size", defaultValue = "10", required = false ) int size) {
        Page<MainCategory> categories = categoryService.findAllMainCategories(page, size);
        PageResponse<?> pageResponse = PageResponse.create(categories);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Main Categories Fetched successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .data(pageResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    @Operation(description = "Create a Main new category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Long categoryId = categoryService.createMainCategory(categoryRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Category created successfully")
                .status(HttpStatus.CREATED.name())
                .statusCode(HttpStatus.CREATED.value())
                .data(categoryId)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{categoryId}/create")
    @Operation(description = "Create a new category in main category")
    public ResponseEntity<?> createSubCategory(
            @Valid @RequestBody CategoryRequest categoryRequest,
            @PathVariable("categoryId") @Valid @Positive Long categoryId
    ) {
        Long id = categoryService.createNewSubCategory(categoryId, categoryRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .message("SubCategory created successfully")
                .status(HttpStatus.CREATED.name())
                .statusCode(HttpStatus.CREATED.value())
                .data(id)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/category/{id}/update")
    @Operation(description = "Update category - Pass categoryId")
    public ResponseEntity<?> updateSubCategory(@Valid @RequestBody CategoryRequest categoryRequest, @PathVariable("id") Long categoryId) {
        categoryService.updateCategory(categoryId, categoryRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .message("SubCategory updated successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/update")
    @Operation(description = "Update Main category - Pass Main categoryId")
    public ResponseEntity<?> updateMainCategory(@Valid @RequestBody CategoryRequest categoryRequest, @PathVariable("id") Long categoryId) {
        categoryService.updateMainCategory(categoryId, categoryRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Main Category updated successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    @Operation(description = "Delete Main category - Pass categoryId")
    public ResponseEntity<?> deleteMainCategory(
            @PathVariable("id") Long categoryId,
            @CurrentUser UserPrincipal user
    ) {
        categoryService.deleteMainCategory(categoryId, user);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Main Category deleted successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/activate")
    @Operation(description = "Activate Main category - Pass Main categoryId")
    public ResponseEntity<?> activateMainCategory(@PathVariable("id") Long categoryId, @CurrentUser UserPrincipal user) {
        categoryService.activateMainCategory(categoryId, user);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Main Category activated successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
