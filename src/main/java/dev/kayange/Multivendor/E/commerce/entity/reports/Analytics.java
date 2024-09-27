package dev.kayange.Multivendor.E.commerce.entity.reports;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "analytics")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime reportDate;  // The date the metrics correspond to

    private int totalOrders;
    private double totalSales;
    private int newCustomers;
    private int returningCustomers;
    private int abandonedCarts;
}
