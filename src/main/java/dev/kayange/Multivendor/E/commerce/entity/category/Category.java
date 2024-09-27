package dev.kayange.Multivendor.E.commerce.entity.category;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "categories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category extends Auditable {
    private String categoryName;
    private String categoryDescription;
    private String categoryIcon;
    //private String imagePath;

    @ManyToOne
    @JoinColumn(name = "main_category_id", nullable = false)
    private MainCategory mainCategory;

}
