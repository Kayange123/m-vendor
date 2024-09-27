package dev.kayange.Multivendor.E.commerce.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.entity.users.Vendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPrincipal implements UserDetails, Principal {
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;
    private String userName;
    private boolean enabled;
    private boolean locked;
    private Boolean changedDefaultPassword;
    private Vendor vendor;
    private LocalDate dateJoined;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isVendor;

    public static UserPrincipal create(Customer user, List<GrantedAuthority> authorityList){
        return UserPrincipal.builder()
                .firstName(user.getFirstName())
                .id(user.getId())
                .userId(user.getUserId())
                .enabled(user.isEnabled())
                .changedDefaultPassword(user.isChangedDefaultPassword())
                .vendor(user.getVendor())
                .dateJoined(LocalDate.from(user.getCreatedAt()))
                .accountNonLocked(user.isLocked())
                .lastName(user.getLastName())
                .userName(user.getUsername())
                .accountNonExpired(user.isAccountNonExpired())
                .authorities(authorityList)
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isAccountNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
