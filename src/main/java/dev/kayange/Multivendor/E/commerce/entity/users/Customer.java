package dev.kayange.Multivendor.E.commerce.entity.users;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import dev.kayange.Multivendor.E.commerce.entity.Wishlist;
import dev.kayange.Multivendor.E.commerce.entity.payment.Order;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer extends Auditable {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String userId;
    private String username;
    private String email;
    private String password;
    private Boolean active;
    private String profileImg;
    private String coverImg;
    private boolean locked;
    private boolean accountNonExpired;
    private boolean enabled = false;
    private boolean changedDefaultPassword;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Vendor vendor;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wishlist> wishlists;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)// foreign key column in Address table
    private List<Address> addresses;

    @Transient
    public String getFullName() {
        return  this.firstName + " "+ this.lastName;
    }
}
