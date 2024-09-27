package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.payment.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT o
        FROM Order o
        WHERE o.orderNumber = :orderNumber
    """)
    Optional<Order> findFirstByOrderNumber(String orderNumber);

    @Query("""
        FROM Order o
        WHERE o.customer.id = :customerId
    """)
    Page<Order> findUserOrders(@Param("customerId") Long customerId, Pageable pageable);
}
