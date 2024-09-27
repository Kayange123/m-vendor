package dev.kayange.Multivendor.E.commerce.dto.response;

import dev.kayange.Multivendor.E.commerce.dto.UserSummary;
import dev.kayange.Multivendor.E.commerce.entity.reviews.Review;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ReviewResponse {
    private Long id;
    private String content;
    private String createdAt;
    private long replies;

    private UserSummary user;

    public static ReviewResponse create(Review review){
        var customer = review.getCustomer();
        return ReviewResponse.builder()
                .content(review.getContent())
                .user(new UserSummary(customer.getUsername(), customer.getFullName(), customer.getEmail(), customer.getProfileImg(), customer.getUserId()))
                .createdAt(review.getCreatedAt().toString())
                .id(review.getId())
                .replies(review.getReplies().size())
                .build();
    }
}
