package dev.kayange.Multivendor.E.commerce.security;

import dev.kayange.Multivendor.E.commerce.dao.CustomerRepository;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.entity.users.UserRole;
import dev.kayange.Multivendor.E.commerce.service.UserRoleService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final UserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

       var customer = customerRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Could not find user by email "+email));

       return getUserDetails(customer);
    }

    public UserDetails loadUserById(Long id){
        var user = customerRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("Could not find user by id "+id));

        return getUserDetails(user);
    }

    @NotNull
    private UserDetails getUserDetails(Customer customer) {

        List<UserRole> userRoles = userRoleService.findAllByUserId(customer.getId());

        List<GrantedAuthority> grantList = new ArrayList<>();

        if (userRoles != null) {
            for (UserRole userRole : userRoles) {
                GrantedAuthority authority = new SimpleGrantedAuthority(userRole.getRole().getName());
                grantList.add(authority);
            }
        }

        return UserPrincipal.create(customer, grantList);
    }
}
