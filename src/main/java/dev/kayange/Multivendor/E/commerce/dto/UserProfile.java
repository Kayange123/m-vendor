package dev.kayange.Multivendor.E.commerce.dto;

import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private boolean enabled;
    private boolean locked;
    private String dateJoined;
    private boolean isVendor;

    public static UserProfile create (UserPrincipal profile){
        return UserProfile.builder()
                .userId(profile.getUserId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .email(profile.getEmail())
                .username(profile.getUsername())
                .enabled(profile.isEnabled())
                .locked(profile.isLocked())
                .dateJoined(profile.getDateJoined().toString())
                .isVendor(profile.getVendor() != null)
                .build();
    }

    public static UserProfile create (Customer profile){
        return UserProfile.builder()
                .userId(profile.getUserId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .email(profile.getEmail())
                .username(profile.getUsername())
                .enabled(profile.isEnabled())
                .locked(profile.isLocked())
                .dateJoined(profile.getCreatedAt().toString())
                .isVendor(profile.getVendor() != null)
                .build();
    }
}
