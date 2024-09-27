package dev.kayange.Multivendor.E.commerce.entity;

import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Token extends Auditable {
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customer user;

    @Transient
    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiresAt);
    }

}
