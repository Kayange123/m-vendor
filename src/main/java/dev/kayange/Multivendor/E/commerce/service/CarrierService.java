package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.dto.request.CarrierRequest;
import dev.kayange.Multivendor.E.commerce.entity.shipment.Carrier;

public interface CarrierService {
    void saveCarrier(CarrierRequest request, Long userId );
    void removeCarrier(Long carrierId, Long userId);
    void updateCarrier(Long carrierId, Long userId);
    Carrier findCarrierById(Long carrierId);
}
