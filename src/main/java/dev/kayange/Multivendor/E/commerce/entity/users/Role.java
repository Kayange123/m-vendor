package dev.kayange.Multivendor.E.commerce.entity.users;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class Role extends Auditable {
    @Column(name = "name", unique = true)
    @NotEmpty(message = "The name of the role is required")
    private String name;
    private String privileges;
}
