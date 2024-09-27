package dev.kayange.Multivendor.E.commerce.entity.product;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "galleries")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageGallery extends Auditable {
    private String imagePath;
    private String type;
    private String altText;
    private Short displayOrder;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
