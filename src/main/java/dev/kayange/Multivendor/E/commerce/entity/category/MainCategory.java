package dev.kayange.Multivendor.E.commerce.entity.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "main_categories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MainCategory extends Auditable {
    private String categoryName;
    private String categoryDescription;
    private String categoryIcon;
    private boolean active;

    @JsonIgnore
    @OneToMany(mappedBy = "mainCategory", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Category> categories;
}
