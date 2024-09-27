package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.dto.request.AddressRequest;
import dev.kayange.Multivendor.E.commerce.entity.users.Address;

public interface AddressService {
    void createAddress(AddressRequest request, Long userId);
    void updateAddress(AddressRequest request, Long addressId, Long userId);
    void deleteAddress(Long addressId, Long userId);
    Address findAddressById(Long addressId);
}
