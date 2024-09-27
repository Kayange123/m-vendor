package dev.kayange.Multivendor.E.commerce.entity.notification;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.enumeration.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification extends Auditable {

    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private String message;
    private boolean read;
    private LocalDateTime notificationExpiryDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customer recipient;

}
