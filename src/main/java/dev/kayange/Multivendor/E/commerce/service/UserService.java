package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.dto.UserProfile;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.entity.users.Vendor;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface UserService {
    Customer findUserById(Long userId);

    Customer findByUserEmail(String email);

    Customer findFirstByUsername(String username);
    Customer findFirstByUserId(String username);

    Boolean userExistsByEmail(String email);

    Boolean usernameExists(String username);

    Customer save(Customer user);

    void upgradeCustomerToVendor(Long userId);

    Page<Vendor> findAllVendors(Pageable pageable);

    Page<Customer> findAllActiveUsers( Pageable pageable);

    Page<Customer> search(String query, Pageable pageable);

    Boolean hasRole(String role, Collection<? extends GrantedAuthority> authorities);

    Boolean hasRole(List<String> roles, Collection<? extends GrantedAuthority> authorities);

    void addRoleToUser(String role, Long userId);

    void addRoleToUser(List<String> roles, Long userId);

    void deleteUserAccount(Long id);

    void lockAccount(Long userId);

    void unlockAccount(Long userId);

    void enableAccount(Long userId);

    int revokeRole(Long userId, Long roleId);

    int revokeAllRoles(Long userId);

    void makeAnAdmin(Long userId, UserPrincipal user);
    void makeSuperAdmin(Long userId , UserPrincipal user);

    void requestToBecomeVendor(UserPrincipal user);

    void verifyVendorAccount(Long id, UserPrincipal user);

    Page<Customer> getAllUsers(int page, int size, String sort, String query);
}
