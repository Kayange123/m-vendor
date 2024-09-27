package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.payment.Order;
import dev.kayange.Multivendor.E.commerce.entity.payment.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
        SELECT item.order
        FROM OrderItem item
        WHERE item.product.id = :id
    """)
    Page<Order> findProductOrders(Long id, Pageable pageable);
}
