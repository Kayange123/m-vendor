package dev.kayange.Multivendor.E.commerce.entity.reviews;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_reviews")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review extends Auditable {
    private double rating;
    private String content;
    private boolean reviewed;
    private boolean hidden;
    private boolean approved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "review")
    private List<ReviewReply> replies = new ArrayList<>();


}
