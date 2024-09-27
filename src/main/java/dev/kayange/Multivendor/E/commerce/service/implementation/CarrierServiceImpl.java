package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dto.request.CarrierRequest;
import dev.kayange.Multivendor.E.commerce.entity.shipment.Carrier;
import dev.kayange.Multivendor.E.commerce.service.CarrierService;
import dev.kayange.Multivendor.E.commerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarrierServiceImpl implements CarrierService {
    private final UserService userService;

    @Override
    public void saveCarrier(CarrierRequest request, Long userId) {

    }

    @Override
    public void removeCarrier(Long carrierId, Long userId) {

    }

    @Override
    public void updateCarrier(Long carrierId, Long userId) {

    }

    @Override
    public Carrier findCarrierById(Long carrierId) {
        return null;
    }
}
