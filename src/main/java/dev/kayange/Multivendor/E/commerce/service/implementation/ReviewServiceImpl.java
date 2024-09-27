package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.ReviewReplyRepository;
import dev.kayange.Multivendor.E.commerce.dao.ReviewRepository;
import dev.kayange.Multivendor.E.commerce.dto.request.ReplyRequest;
import dev.kayange.Multivendor.E.commerce.dto.request.ReviewRequest;
import dev.kayange.Multivendor.E.commerce.entity.reviews.Review;
import dev.kayange.Multivendor.E.commerce.entity.reviews.ReviewReply;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.exception.ResourceNotFoundException;
import dev.kayange.Multivendor.E.commerce.service.ProductService;
import dev.kayange.Multivendor.E.commerce.service.ReviewService;
import dev.kayange.Multivendor.E.commerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewReplyRepository replyRepository;
    private final ProductService productService;
    private final UserService userService;

    @Override
    public void saveReview(ReviewRequest review, Long userId) {
        var user = userService.findUserById(userId);
        var product = productService.findProductById(review.productId());
        var reviewBuilder = Review.builder().reviewed(false).approved(false).hidden(false)
                .product(product)
                .customer(user)
                .content(review.content())
                .rating(review.rating() > 5 ? 5 : review.rating())
                .build();

        reviewRepository.save(reviewBuilder);
    }

    @Override
    public void saveReviewReply(ReplyRequest reply, Long userId) {
        var review = getReviewById(reply.reviewId());
        var user = userService.findUserById(userId);
        var replyBuilder = ReviewReply.builder()
                .content(reply.content())
                .review(review)
                .customer(user)
                .hidden(false)
                .build();
        replyRepository.saveAndFlush(replyBuilder);
    }

    @Override
    public Page<Review> findAllProductReviews(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findAllReviews(productId, pageable);
    }

    @Override
    public Page<ReviewReply> findAllReviewReplies(Long reviewId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return replyRepository.findAllReviewReplies(reviewId, pageable);
    }

    @Override
    public Optional<Review> findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    private Review getReviewById(Long reviewId) {
        return findReviewById(reviewId).orElseThrow(()-> new ResourceNotFoundException("Review ID not found"));
    }

    @Override
    public ReviewReply findReviewReplyById(Long replyId) {
        return replyRepository.findById(replyId).orElseThrow(()-> new ResourceNotFoundException("Review Reply is Not found"));
    }

    @Override
    public void hideProductReview(Long reviewId, Long userId) {
        var user = userService.findUserById(userId);
        var review = getReviewById(reviewId);
        boolean b = Objects.equals(user.getId(), review.getProduct().getVendor().getCustomer().getId());
        if(!b) throw new ApiException("Only product owners can hide review");
        review.setHidden(true);
        reviewRepository.saveAndFlush(review);
    }

    @Override
    //@PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public void previewProductReview(Long reviewId, Long userId) {
        var user = userService.findUserById(userId);
        var review = getReviewById(reviewId);
        review.setHidden(false);
        review.setApproved(true);
        review.setReviewed(true);
        review.setUpdatedBy(user.getId());
        reviewRepository.saveAndFlush(review);
    }

    @Override
    public void hideReviewReply(Long replyId, Long userId) {
        var user = userService.findUserById(userId);
        var reply = findReviewReplyById(replyId);
        var review = reply.getReview();
        boolean b = Objects.equals(user.getId(), review.getProduct().getVendor().getCustomer().getId());
        if(!b) throw new ApiException("Only product owners can hide review");
        reply.setHidden(true);
        replyRepository.save(reply);
    }

    @Override
    public void deleteProductReview(Long reviewId, Long userId) {
        var user = userService.findUserById(userId);
        var review = getReviewById(reviewId);
        if(!Objects.equals(user.getId(), review.getCustomer().getId())) throw new ApiException("Only review creator can delete");
        reviewRepository.delete(review);
    }

    @Override
    public void deleteReviewReply(Long replyId, Long userId) {
        var user = userService.findUserById(userId);
        var reply = findReviewReplyById(replyId);
        boolean isCreator = Objects.equals(user.getId(), reply.getCustomer().getId());
        if(!isCreator) throw new ApiException("Only reply creator can delete replies");
        replyRepository.delete(reply);
    }
}
