package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
