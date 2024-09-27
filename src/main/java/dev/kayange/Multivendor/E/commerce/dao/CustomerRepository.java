package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Optional<Customer> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<Customer> findFirstByUsername(String username);

    Optional<Customer> findFirstByUserId(String userId);

    @Query("""
        FROM Customer c
        WHERE c.enabled = true AND c.active = true AND c.isDeleted = false
    """)
    Page<Customer> findAllActiveCustomers(Pageable pageable);

    @Query("""
        FROM Customer c
        WHERE (
        lower(c.firstName) LIKE %:query% OR
        lower(c.lastName) LIKE %:query% OR
        lower(c.email) LIKE %:query%)
        ORDER BY c.firstName ASC
    """)
    Page<Customer> search(String query, Pageable pageable);

    int countAllByUsernameIgnoreCase(String username);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from user_roles where user_id = ?1 AND role_id = ?2", nativeQuery = true)
    int revokeRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM user_roles WHERE user_id = ?1", nativeQuery = true)
    int revokeAllRoles(Long userId);

    @Query("""
        FROM Customer c
        WHERE (lower(c.firstName) LIKE %:query% OR lower(c.lastName) LIKE %:query% OR lower(c.username) LIKE %:query%)
    """)
    Page<Customer> findAllByName(@Param("query") String query, Pageable pageable);
}
