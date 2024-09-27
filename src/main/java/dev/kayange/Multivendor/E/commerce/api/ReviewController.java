package dev.kayange.Multivendor.E.commerce.api;

import dev.kayange.Multivendor.E.commerce.dto.PageResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.ApiResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.ReplyRequest;
import dev.kayange.Multivendor.E.commerce.dto.request.ReviewRequest;
import dev.kayange.Multivendor.E.commerce.dto.response.ReplyResponse;
import dev.kayange.Multivendor.E.commerce.dto.response.ReviewResponse;
import dev.kayange.Multivendor.E.commerce.entity.reviews.Review;
import dev.kayange.Multivendor.E.commerce.entity.reviews.ReviewReply;
import dev.kayange.Multivendor.E.commerce.security.CurrentUser;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import dev.kayange.Multivendor.E.commerce.service.ReviewService;
import dev.kayange.Multivendor.E.commerce.utils.AppConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reviews")
@Tag(name = "Reviews", description = "Feedbacks for products and their replies")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/create")
    @Operation(summary = "Create a review to a product")
    public ResponseEntity<?> createReview(@RequestBody @Valid ReviewRequest request, @CurrentUser UserPrincipal user){
        reviewService.saveReview(request, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Review created successfully")
                .status(HttpStatus.CREATED.name())
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/reply/create")
    @Operation(summary = "Create a reply to a review")
    public ResponseEntity<?> createReviewReply(@RequestBody @Valid ReplyRequest request, @CurrentUser UserPrincipal user){
        reviewService.saveReviewReply(request, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Reply created successfully")
                .status(HttpStatus.CREATED.name())
                .statusCode(HttpStatus.CREATED.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{reviewId}/approve")
    @Operation(summary = "Approve a newly created review")
    public ResponseEntity<?> approveReview(@PathVariable("reviewId") Long reviewId, @CurrentUser UserPrincipal user){
        reviewService.previewProductReview(reviewId, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Review was reviewed successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{reviewId}/hide")
    @Operation(summary = "Hide review from product - Only For product owners")
    public ResponseEntity<?> hideReview(@PathVariable("reviewId") Long reviewId, @CurrentUser UserPrincipal user){
        reviewService.hideProductReview(reviewId, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Review was hidden successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reply/{replyId}/hide")
    @Operation(summary = "Hide reply from product Review - Only For product owners")
    public ResponseEntity<?> hideReviewReply(@PathVariable("replyId") Long replyId, @CurrentUser UserPrincipal user){
        reviewService.hideReviewReply(replyId, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Review Reply was hidden successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{reviewId}")
    @Operation(summary = "Delete review from product - For review creator")
    public ResponseEntity<?> deleteReview(@PathVariable("reviewId") Long reviewId, @CurrentUser UserPrincipal user){
        reviewService.deleteProductReview(reviewId, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Review was deleted successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/reply/{replyId}")
    @Operation(summary = "Delete Reply from product Review - Only reply creator")
    public ResponseEntity<?> deleteReviewReply(@PathVariable("replyId") Long replyId, @CurrentUser UserPrincipal user){
        reviewService.deleteReviewReply(replyId, user.getId());
        ApiResponse<?> response = ApiResponse.builder()
                .message("Review Reply was deleted successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Retrieve all product Reviews")
    public ResponseEntity<?> getAllProductReviews(@PathVariable("productId") Long productId, @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int page, @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int size){
        Page<Review> reviews = reviewService.findAllProductReviews(productId, page, size);

        var data = reviews.stream().map(ReviewResponse::create).toList();
        var pageResponse = PageResponse.builder().pageNumber(reviews.getNumber()).data(data)
                .totalElements(reviews.getTotalElements())
                .totalPages(reviews.getTotalPages()).last(reviews.isLast())
                .first(reviews.isFirst()).pageSize(reviews.getSize()).build();

        ApiResponse<?> response = ApiResponse.builder()
                .message("Reviews retrieved successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .data(pageResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "Retrieve all replies by review")
    public ResponseEntity<?> getAllReviewReplies(@PathVariable("reviewId") Long reviewId, @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER, required = false) int page, @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE, required = false) int size){
        Page<ReviewReply> replies = reviewService.findAllReviewReplies(reviewId, page, size);

        var data = replies.stream().map(ReplyResponse::create).toList();
        var pageResponse = PageResponse.builder().pageNumber(replies.getNumber()).data(data)
                .totalElements(replies.getTotalElements())
                .totalPages(replies.getTotalPages()).last(replies.isLast())
                .first(replies.isFirst()).pageSize(replies.getSize()).build();

        ApiResponse<?> response = ApiResponse.builder()
                .message("Replies retrieved successfully")
                .status(HttpStatus.OK.name())
                .statusCode(HttpStatus.OK.value())
                .data(pageResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
