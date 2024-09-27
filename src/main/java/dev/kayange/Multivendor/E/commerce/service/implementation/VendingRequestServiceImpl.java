package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.VendingRequestRepository;
import dev.kayange.Multivendor.E.commerce.entity.VendingRequest;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.service.VendingRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VendingRequestServiceImpl implements VendingRequestService {
    private final VendingRequestRepository vendingRequestRepository;

    @Override
    public void saveRequest(VendingRequest request) {
        vendingRequestRepository.save(request);
    }

    @Override
    public VendingRequest findRequestByUserId(Long userId) {
        return getRequestByUserId(userId).orElseThrow(
                ()-> new ApiException("Could not find request by user id " + userId)
        );
    }

    @Override
    public Optional<VendingRequest> getRequestByUserId(Long userId) {
        return vendingRequestRepository.findByUserId(userId);
    }
}
