package dev.kayange.Multivendor.E.commerce.api;

import dev.kayange.Multivendor.E.commerce.dto.OrderResponse;
import dev.kayange.Multivendor.E.commerce.dto.PageResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.ApiResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.OrderRequest;
import dev.kayange.Multivendor.E.commerce.entity.payment.Order;
import dev.kayange.Multivendor.E.commerce.security.CurrentUser;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import dev.kayange.Multivendor.E.commerce.service.OrderService;
import dev.kayange.Multivendor.E.commerce.utils.AppConstant;
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
@Tag(name = "Orders", description = "The Orders component API endpoints")
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/place-order")
    @Operation(summary = "Create a new Order")
    public ResponseEntity<?> createNewProduct(
            @RequestBody @Valid OrderRequest productRequest,
            @CurrentUser UserPrincipal user
    ){
        orderService.createAnOrder(productRequest, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Order placed successfully")
                .status(HttpStatus.CREATED.name())
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    @Operation(summary = "Retrieve all logged in user orders")
    public ResponseEntity<?> getAllUserOrders(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int size,
            @CurrentUser UserPrincipal user
            ){
        Page<Order> orders = orderService.findUserOrders(user.getId(), page, size);
        return getOrderResponse(orders);
    }

    @GetMapping("")
    @Operation(summary = "Retrieve all orders")
    public ResponseEntity<?> getAllOrders(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int size
    ){
        Page<Order> orders = orderService.findAllOrders(page, size);
        return getOrderResponse(orders);
    }

    @GetMapping("/{orderNumber}")
    @Operation(summary = "Retrieve Order by OrderNumber")
    public ResponseEntity<?> getOrderByNumber(@PathVariable("orderNumber") String orderNumber){
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        ApiResponse<?> response = ApiResponse.builder()
                .message("Order retrieved successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .data(OrderResponse.create(order))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Retrieve all orders")
    public ResponseEntity<?> getProductOrders(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int size,
            @PathVariable Long productId
    ){
        Page<Order> orders = orderService.findProductOrders(productId, page, size);
        return getOrderResponse(orders);
    }

    @DeleteMapping("/{orderNumber}")
    @Operation(summary = "Delete Order by OrderNumber")
    public ResponseEntity<?> deleteOrderByNumber(
            @PathVariable("orderNumber") String orderNumber,
            @CurrentUser UserPrincipal user
    ){
        orderService.deleteAnOrder(orderNumber, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Order deleted successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static ResponseEntity<? extends ApiResponse<?>> getOrderResponse(Page<Order> orders) {
        var orderData = orders.stream().map(OrderResponse::create).toList();
        var data = PageResponse.builder().pageNumber(orders.getNumber()).data(orderData)
                .totalElements(orders.getTotalElements())
                .totalPages(orders.getTotalPages()).last(orders.isLast())
                .first(orders.isFirst()).pageSize(orders.getSize()).build();

        ApiResponse<?> response = ApiResponse.builder()
                .message("Orders retrieved successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .data(data)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
