package dev.kayange.Multivendor.E.commerce.entity.reviews;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "reviews_replies")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewReply extends Auditable {
    private String content;
    private boolean hidden;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
