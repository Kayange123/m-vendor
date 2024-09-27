package dev.kayange.Multivendor.E.commerce.entity.users;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "addresses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address extends Auditable {

    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customer user;
}
