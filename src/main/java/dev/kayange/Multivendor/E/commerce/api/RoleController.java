package dev.kayange.Multivendor.E.commerce.api;

import dev.kayange.Multivendor.E.commerce.dto.PageResponse;
import dev.kayange.Multivendor.E.commerce.dto.RoleResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.ApiResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.RoleRequest;
import dev.kayange.Multivendor.E.commerce.entity.users.Role;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.exception.ResourceNotFoundException;
import dev.kayange.Multivendor.E.commerce.service.RoleService;
import dev.kayange.Multivendor.E.commerce.utils.AppConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("roles")
@Tag(name = "User Roles", description = "Roles associated with system users")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @Operation(description = "Find All roles")
    public ResponseEntity<?> findAll() {
        List<Role> roles = roleService.findAll();
        ApiResponse<?> response = ApiResponse.builder()
                .message("Roles Fetched successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .data(roles.stream().map(RoleResponse::create).toList())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/page")
    @Operation(description = "Find All roles by pages and sort by Id")
    public ResponseEntity<?> findAll(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "query", defaultValue = "_", required = false) String query
    ) {
        Page<Role> items;
        if (query.equals("_") || query.isEmpty()) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            items = roleService.findAll(pageable);
        } else {
            Pageable pageable = PageRequest.of(0, size, Sort.by(sortBy).ascending());
            items = roleService.findAll(query.toLowerCase(), pageable);
        }
        PageResponse<?> response = PageResponse.builder()
                .data(items.get().map(RoleResponse::create).toList())
                .totalPages(items.getTotalPages())
                .last(items.isLast()).totalElements(items.getTotalElements())
                .first(items.isFirst()).pageSize(items.getSize())
                .pageNumber(items.getNumber())
                .build();
        var res = ApiResponse.builder()
                .data(response)
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .message("Roles fetched successfully")
                .build();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/all/except")
    @Operation(description = "Get all roles except for specified ones")
    public ResponseEntity<?> getAllExcept(@RequestParam(value = "query", defaultValue = "_") String query,
                                          @RequestParam(value = "exceptIds") List<Long> exceptIds) {
        List<Role> items;
        if (query.equals("_") || query.isEmpty()) {
            items = roleService.findAllExcept(exceptIds);
        } else {
            items = roleService.findAllExcept(query.toLowerCase(), exceptIds);
        }

        var response = ApiResponse.builder()
                .data(items.stream().map(RoleResponse::create).toList())
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .message("Roles fetched successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")
    @Operation(description = "Create a new role")
    public ResponseEntity<?> create(@Valid @RequestBody RoleRequest roleRequest) {
        roleService.saveRole(roleRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Role created successfully")
                .status(HttpStatus.CREATED.name())
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(description = "Fetch a Role by its ID")
    public ResponseEntity<ApiResponse<RoleResponse>> findById(@PathVariable Long id) {
        Role role = roleService.findById(id).orElseThrow(()-> new ResourceNotFoundException("Role " + id + " does not exist"));
        ApiResponse<RoleResponse> response = ApiResponse
                .<RoleResponse>builder()
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .message("Role fetched successfully")
                .data(RoleResponse.create(role))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Role role) {
        var roleOptional = roleService.findById(id).orElseThrow(()-> new ApiException("Role with ID "+id +" not found"));

        if(Objects.nonNull(roleOptional.getName())){
            roleOptional.setName(role.getName());
        }
        roleService.save(roleOptional);
        ApiResponse<Role> response = ApiResponse
                .<Role>builder()
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .message("Role modified successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Role role = roleService.findById(id).orElseThrow(()-> new ResourceNotFoundException("Role " + id + " does not exist"));
        roleService.delete(role.getId());
        ApiResponse<Role> response = ApiResponse
                .<Role>builder()
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .message("Role deleted successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
