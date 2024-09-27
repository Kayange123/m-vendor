package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.entity.VendingRequest;

import java.util.Optional;

public interface VendingRequestService {
    void saveRequest(VendingRequest request);

    VendingRequest findRequestByUserId(Long userId);

    Optional<VendingRequest> getRequestByUserId(Long userId);
}
