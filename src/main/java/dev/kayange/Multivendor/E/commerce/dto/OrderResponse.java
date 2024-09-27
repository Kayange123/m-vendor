package dev.kayange.Multivendor.E.commerce.dto;

import dev.kayange.Multivendor.E.commerce.entity.payment.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private String orderNumber;
    private boolean isShippingFree;
    private double taxAmount;
    private double totalAmount;
    private String orderDate;
    private UserSummary customer;
    private List<Item> items;

    public static OrderResponse create(Order order){
        var cus = order.getCustomer();
        return OrderResponse.builder()
                .customer(new UserSummary(cus.getUsername(), cus.getFullName(), cus.getEmail(), cus.getProfileImg(), cus.getUserId()))
                .orderNumber(order.getOrderNumber())
                .isShippingFree(order.isShippingFree())
                .taxAmount(order.getTaxAmount().doubleValue())
                .totalAmount(order.getTotalOrderAmount())
                .orderDate(order.getOrderDate().toString())
                .items(order.getOrderItems().stream()
                        .map(item->new Item(
                                item.getQuantity(),
                                item.getPrice(),
                                item.getProduct().getProductName()
                        )).toList())
                .build();
    }
}

record Item(int quantity, double price, String productName){

}

