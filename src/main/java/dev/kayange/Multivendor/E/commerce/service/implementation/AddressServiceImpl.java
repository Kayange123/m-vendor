package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.AddressRepository;
import dev.kayange.Multivendor.E.commerce.dto.request.AddressRequest;
import dev.kayange.Multivendor.E.commerce.entity.users.Address;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.exception.ResourceNotFoundException;
import dev.kayange.Multivendor.E.commerce.service.AddressService;
import dev.kayange.Multivendor.E.commerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserService userService;

    @Override
    public void createAddress(AddressRequest request, Long userId) {
        var user = userService.findUserById(userId);
        var address = Address.builder()
                .user(user).city(request.city())
                .street(request.street())
                .state(request.city()).country(request.country())
                .postalCode(request.postalCode()).build();

        addressRepository.saveAndFlush(address);
    }

    @Override
    public void updateAddress(AddressRequest request, Long addressId, Long userId) {
        var address = findAddressById(addressId);

        if(!Objects.equals(address.getUser().getId(), userId)){throw new ApiException("You are not allowed to edit other's address");}

        if(Objects.nonNull(request.street())){
            address.setStreet(request.street());
        }
        if (Objects.nonNull(request.postalCode())) {
            address.setPostalCode(request.postalCode());
        }
        if (Objects.nonNull(request.country())) {
            address.setCountry(request.country());
        }
        if (Objects.nonNull(request.city())) {
            address.setCity(request.city());
            address.setState(request.city());
        }

        addressRepository.saveAndFlush(address);
    }

    @Override
    public void deleteAddress(Long addressId, Long userId) {
        var address = findAddressById(addressId);
        if(!Objects.equals(address.getUser().getId(), userId)){throw new ApiException("You are not allowed to delete other's address");}
        addressRepository.delete(address);
    }

    @Override
    public Address findAddressById(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(
                ()-> new ResourceNotFoundException("Address not found")
        );
    }
}
