package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.dto.request.ReplyRequest;
import dev.kayange.Multivendor.E.commerce.dto.request.ReviewRequest;
import dev.kayange.Multivendor.E.commerce.entity.reviews.Review;
import dev.kayange.Multivendor.E.commerce.entity.reviews.ReviewReply;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ReviewService {
    void saveReview(ReviewRequest review, Long userId);
    void saveReviewReply(ReplyRequest reply , Long userId);
    Page<Review> findAllProductReviews(Long productId, int page, int size);
    Page<ReviewReply> findAllReviewReplies(Long reviewId, int page, int size);
    Optional<Review> findReviewById(Long reviewId);
    ReviewReply findReviewReplyById(Long replyId);
    void hideProductReview(Long reviewId,  Long userId);
    void previewProductReview(Long reviewId , Long userId);
    void hideReviewReply(Long replyId , Long userId);
    void deleteProductReview(Long reviewId, Long userId);
    void deleteReviewReply(Long replyId, Long userId);
}
