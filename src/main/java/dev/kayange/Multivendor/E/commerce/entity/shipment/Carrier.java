package dev.kayange.Multivendor.E.commerce.entity.shipment;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carriers")
public class Carrier extends Auditable {
    private String name;
    private String phone;
    private String email;
}
