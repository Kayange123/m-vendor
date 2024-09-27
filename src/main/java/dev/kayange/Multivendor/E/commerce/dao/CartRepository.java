package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("""
            SELECT c
            FROM Cart c
            WHERE c.customer.id = :customerId
    """)
    Optional<Cart> findCartByCustomerId(Long customerId);

    @Query("""
            SELECT c
            FROM Cart c
            WHERE c.customer.userId = :userId
    """)
    Optional<Cart> findCartByCustomerUserId(@Param("userId") String userId);
}
