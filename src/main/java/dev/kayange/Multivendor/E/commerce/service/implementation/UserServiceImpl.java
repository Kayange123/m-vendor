package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.CustomerRepository;
import dev.kayange.Multivendor.E.commerce.dao.VendorRepository;
import dev.kayange.Multivendor.E.commerce.dto.UserProfile;
import dev.kayange.Multivendor.E.commerce.entity.VendingRequest;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.entity.users.Role;
import dev.kayange.Multivendor.E.commerce.entity.users.UserRole;
import dev.kayange.Multivendor.E.commerce.entity.users.Vendor;
import dev.kayange.Multivendor.E.commerce.enumeration.SystemUserRole;
import dev.kayange.Multivendor.E.commerce.enumeration.VendingStatus;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.exception.ResourceNotFoundException;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import dev.kayange.Multivendor.E.commerce.service.RoleService;
import dev.kayange.Multivendor.E.commerce.service.UserRoleService;
import dev.kayange.Multivendor.E.commerce.service.UserService;
import dev.kayange.Multivendor.E.commerce.service.VendingRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements  UserService {
    public static final String BIO = "Hi there! Welcome to the official account of @";
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final VendingRequestService vendingRequestService;

    @Override
    public Customer findUserById(Long userId) {
        return customerRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User with ID " + userId+ " not found")
        );
    }

    @Override
    public Customer findByUserEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("User with email " + email + " not found")
        );
    }

    @Override
    public Customer findFirstByUsername(String username) {return customerRepository.findFirstByUsername(username).orElse(null);}

    @Override
    public Customer findFirstByUserId(String username) {return customerRepository.findFirstByUserId(username).orElseThrow(()-> new ResourceNotFoundException("No Vendor found with ID "+username));}

    @Override
    public Boolean userExistsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public Customer save(Customer user){
        return customerRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void upgradeCustomerToVendor(Long customerId) {
        var user = findUserById(customerId);
        var vendor = Vendor.builder().customer(user).suspended(false).name(user.getFullName()).build();
        Vendor savedVendor = saveVendor(vendor);
        user.setVendor(savedVendor);
        save(user);
    }

    private Vendor saveVendor(Vendor customer) {
        return vendorRepository.saveAndFlush(customer);
    }

    @Override
    public Page<Vendor> findAllVendors(Pageable pageable) {
        return vendorRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> findAllActiveUsers(Pageable pageable) {
        return customerRepository.findAllActiveCustomers(pageable);
    }

    @Override
    public Page<Customer> search(String query, Pageable pageable) {
        return customerRepository.search(query, pageable);
    }

    @Override
    public Boolean hasRole(String role, Collection<? extends GrantedAuthority> authorities) {
        boolean exists = false;
        for (GrantedAuthority authority : authorities) {
            exists = authority.getAuthority().equals(role);
        }
        return exists;
    }

    @Override
    public Boolean hasRole(List<String> roles, Collection<? extends GrantedAuthority> authorities) {
        boolean exists = false;
        for (GrantedAuthority authority : authorities) {
            for (String role : roles) {
                if (authority.getAuthority().equals(role)) exists = true;
            }
        }
        return exists;
    }

    @Override
    public void addRoleToUser(String roleName, Long userId) {
        Customer user = findUserById(userId);
        Role role = roleService.findFirstByName(roleName);
        Boolean exists = userRoleService.exists(user.getId(), role.getId());
        if(exists) return;

        var userRole = UserRole.builder().role(role).user(user).build();
        userRoleService.save(userRole);
    }

    @Override
    public void addRoleToUser(List<String> roles, Long userId) {
        for (String roleName : roles) {
            addRoleToUser(roleName , userId);
        }
    }

    @Override
    public void deleteUserAccount(Long id) {
        //Disable the Account first and start cronJob to delete after 30 days
        Customer user = findUserById(id);
        user.setActive(false);
        user.setLocked(true);
        user.setEnabled(false);
        user.setDeleted(true);
        save(user);
        // TODO: 01/09/2024 initiate cronJob to Delete account after 30 Days
    }

    @Override
    public void lockAccount(Long userId) {
        Customer user = findUserById(userId);
        user.setLocked(true);
        save(user);
    }

    @Override
    public void unlockAccount(Long userId) {
        Customer user = findUserById(userId);
        user.setLocked(false);
        user.setEnabled(true);
        user.setActive(true);
        user.getVendor().setSuspended(false);
        save(user);
    }

    @Override
    public void enableAccount(Long userId) {
        Customer user = findUserById(userId);
        user.setEnabled(true);
        user.setActive(true);
        save(user);
    }

    public Boolean usernameExists(String username) {
        return customerRepository.countAllByUsernameIgnoreCase(username) > 1;
    }

    @Override
    public int revokeRole(Long userId, Long roleId) {
        return customerRepository.revokeRole(userId, roleId);
    }

    @Override
    public int revokeAllRoles(Long userId) {
        return customerRepository.revokeAllRoles(userId);
    }

    @Override
    public void makeAnAdmin(Long userId , UserPrincipal user) {
       var roles = List.of(SystemUserRole.ADMIN.getRoleName(), SystemUserRole.VENDOR.getRoleName());
       addRoleToUser(roles, userId);
    }

    @Override
    public void makeSuperAdmin(Long userId , UserPrincipal user) {
        var roles = List.of(SystemUserRole.ADMIN.getRoleName(), SystemUserRole.MANAGER.getRoleName(), SystemUserRole.VENDOR.getRoleName(), SystemUserRole.SUPER_ADMIN.getRoleName());
        addRoleToUser(roles, userId);
    }

    @Override
    public void requestToBecomeVendor(UserPrincipal user) {
        String vendorRole = SystemUserRole.VENDOR.getRoleName();
        Boolean hasRole = hasRole(vendorRole, user.getAuthorities());
        Customer customer = findUserById(user.getId());
        var requestByUserId = vendingRequestService.getRequestByUserId(user.getId());

        if (!hasRole && requestByUserId.isEmpty()) {
            var request = VendingRequest.builder()
                    .status(VendingStatus.PENDING)
                    .customer(customer)
                    .build();

            vendingRequestService.saveRequest(request);
        }
    }

    @Override
    public void verifyVendorAccount(Long id, UserPrincipal user) {
        Customer customer = findUserById(id);
        var request = vendingRequestService.findRequestByUserId(customer.getId());
        if(!request.getStatus().name().equals(VendingStatus.PENDING.name())) return;
        request.setStatus(VendingStatus.ACCEPTED);

        var vendor = Vendor.builder()
                .name(customer.getFullName())
                .activated(true).customer(customer)
                .suspended(false).bio(BIO.concat(customer.getUsername())).build();
        Vendor savedVendor = vendorRepository.save(vendor);
        customerRepository.save(customer);
        vendingRequestService.saveRequest(request);
    }

    @Override
    public Page<Customer> getAllUsers(int page, int size, String sortBy, String query) {
        Page<Customer> users;
        String sort = sortBy;
        if(!sortBy.equals("id")){sort = "createdAt";}
        if (query.equals("_") || query.isEmpty()) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
            users = customerRepository.findAll(pageable);
        } else {
            Pageable pageable = PageRequest.of(0, size, Sort.by(sort).ascending());
            users = customerRepository.findAllByName(query.toLowerCase(), pageable);
        }
        return users;
    }

    private Vendor findVendorById(Long id) {
        return vendorRepository.findById(id).orElseThrow(
                ()-> new ApiException("Could not find vendor")
        );
    }

}
