package dev.kayange.Multivendor.E.commerce.entity.users;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import jakarta.persistence.*;
import lombok.*;

/**
 *
 * @author Mr kayange <kayangejr3@gmail.com>
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "user_roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})})
public class UserRole extends Auditable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Customer user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
}
