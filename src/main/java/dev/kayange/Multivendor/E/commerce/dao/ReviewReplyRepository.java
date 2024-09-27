package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.reviews.ReviewReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {

    @Query("""
        FROM ReviewReply r
        WHERE r.review.id = :reviewId
        AND r.hidden = false
        AND r.isDeleted = false
        ORDER BY r.createdAt ASC
    """)
    Page<ReviewReply> findAllReviewReplies(Long reviewId, Pageable pageable);
}
