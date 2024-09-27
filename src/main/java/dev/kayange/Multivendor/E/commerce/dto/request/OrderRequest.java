package dev.kayange.Multivendor.E.commerce.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderRequest {
    private boolean deliveryAvailable;
    private BigDecimal taxAmount;
    private List<OrderItemRequest> items;
}

