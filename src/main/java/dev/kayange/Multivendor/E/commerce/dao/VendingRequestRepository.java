package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.VendingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendingRequestRepository extends JpaRepository<VendingRequest, Long> {

    @Query("""
        SELECT c
        FROM VendingRequest c
        WHERE c.customer.id = :userId
    """)
    Optional<VendingRequest> findByUserId(Long userId);
}
