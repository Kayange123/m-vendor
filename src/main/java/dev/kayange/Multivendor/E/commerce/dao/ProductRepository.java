package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        From Product p
        WHERE p.publishable = true AND p.onSale = true
        ORDER BY p.createdAt DESC
    """)
    Page<Product> findAllProducts(Pageable pageable);

    @Query("""
        FROM Product p
        WHERE p.category.id = :categoryId
        AND p.publishable = true
        AND p.onSale = true
        AND p.isDeleted = false
        GROUP BY p.promoted
        ORDER BY p.createdAt DESC
    """)
    Page<Product> findProductsByCategory(Long categoryId, Pageable pageable);

    @Query("""
        FROM Product p
        WHERE p.vendor.id = :id
        AND p.publishable = true
        AND p.onSale = true
        AND p.isDeleted = false
        ORDER BY p.basePrice DESC
    """)
    Page<Product> findProductsByHost(Long id, Pageable pageable);

    @Query("""
        FROM Product p
        WHERE p.featured = true
        AND p.publishable = true
        AND p.onSale = true
        AND p.isDeleted = false
        ORDER BY p.basePrice DESC
    """)
    Page<Product> findFeaturedProducts(Pageable pageable);

    @Query("""
        FROM Product p
        WHERE p.promoted = true
        AND p.publishable = true
        AND p.onSale = true
        AND p.isDeleted = false
        ORDER BY p.basePrice DESC
    """)
    Page<Product> findPromotedProducts(Pageable pageable);

    @Query("""
        FROM Product p
        WHERE p.promoted = true
        OR p.promoted = true
        AND p.publishable = true
        AND p.onSale = true
        AND p.isDeleted = false
        ORDER BY p.basePrice DESC
    """)
    Page<Product> findFeaturedPromotedProducts(Pageable pageable);

    @Query("""
        FROM Product p
        WHERE p.publishable = true
        AND p.onSale = true
        AND p.isDeleted = false
        AND (lower(p.productName) LIKE %:query%
        OR lower(p.productDescription) LIKE %:query%
        OR lower(p.shortDescription) LIKE %:query%)
        ORDER BY p.basePrice ASC
    """)
    Page<Product> findAllProductsByName(@Param("query") String query, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.publicId = ?1")
    Optional<Product> findFirstByPublicId(String publicId);
}
