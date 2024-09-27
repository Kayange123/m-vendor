package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.reviews.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    @Query("""
        FROM Review r
        WHERE r.product.id = :productId
        AND r.approved = true
        AND r.hidden = false
        ORDER BY r.createdAt ASC
    """)
    Page<Review> findAllReviews(Long productId, Pageable pageable);
}
