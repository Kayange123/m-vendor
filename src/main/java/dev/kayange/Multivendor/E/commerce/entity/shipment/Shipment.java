package dev.kayange.Multivendor.E.commerce.entity.shipment;

import dev.kayange.Multivendor.E.commerce.entity.Auditable;
import dev.kayange.Multivendor.E.commerce.entity.payment.Order;
import dev.kayange.Multivendor.E.commerce.entity.users.Address;
import dev.kayange.Multivendor.E.commerce.enumeration.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shipments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Shipment extends Auditable {
    private String trackingNumber;
    @OneToOne
    @JoinColumn(name = "carrier_id")
    private Carrier carrier;
    private LocalDateTime shipmentDate;
    private LocalDateTime deliveryDate;
    @OneToOne
    @JoinColumn(name = "billing_address_id")
    private Address sourceAddress;
    @OneToOne
    @JoinColumn(name = "shipping_address_id")
    private Address deliveryAddress;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    @OneToMany
    private List<Order> orders;
}
