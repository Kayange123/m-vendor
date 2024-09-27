package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.product.ProductSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductSkuRepository extends JpaRepository<ProductSku, Long> {

    @Query("""
        FROM ProductSku sku
        WHERE sku.product.id = :productId
    """)
    Optional<ProductSku> findByProductId(Long productId);

    @Query("""
        FROM ProductSku sku
        WHERE sku.product.publicId = :productId
    """)
    Optional<ProductSku> findByProductId(String productId);
}
