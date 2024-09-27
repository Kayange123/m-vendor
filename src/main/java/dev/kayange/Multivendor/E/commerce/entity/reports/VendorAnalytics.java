package dev.kayange.Multivendor.E.commerce.entity.reports;

import dev.kayange.Multivendor.E.commerce.entity.users.Vendor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendor_analytics")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private LocalDateTime reportDate;

    private double totalSales;
    private int totalOrders;
    private double returnRate;
    private double averageRating;
}
