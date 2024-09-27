package dev.kayange.Multivendor.E.commerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tags")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tag extends Auditable {
    private String tagName;
    private String icon;
    private Integer quantity;
}
