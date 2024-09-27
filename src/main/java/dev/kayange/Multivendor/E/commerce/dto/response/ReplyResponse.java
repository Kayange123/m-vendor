package dev.kayange.Multivendor.E.commerce.dto.response;

import dev.kayange.Multivendor.E.commerce.dto.UserSummary;
import dev.kayange.Multivendor.E.commerce.entity.reviews.ReviewReply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ReplyResponse {
    private Long id;
    private String content;
    private String createdAt;
    private UserSummary user;

    public static ReplyResponse create(ReviewReply reply){
        return ReplyResponse.builder()
                .id(reply.getId())
                .createdAt(reply.getCreatedAt().toString())
                .content(reply.getContent())
                .user(new UserSummary(reply.getCustomer().getUsername(), reply.getCustomer().getFullName(), reply.getCustomer().getEmail(), reply.getCustomer().getProfileImg(), reply.getCustomer().getUserId()))
                .build();
    }
}
