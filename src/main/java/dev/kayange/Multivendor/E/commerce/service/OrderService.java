package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.dto.request.OrderRequest;
import dev.kayange.Multivendor.E.commerce.entity.payment.Order;
import org.springframework.data.domain.Page;

public interface OrderService {
    void createAnOrder(OrderRequest order, Long userId);
    void deleteAnOrder(String orderNumber, long userId);
    Order findOrderByOrderNumber(String orderNumber);
    Page<Order> findAllOrders(int page, int size);
    Page<Order> findUserOrders(long userId, int page, int size);
    Page<Order> findProductOrders(long productId, int page, int size);
}
