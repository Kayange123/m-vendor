package dev.kayange.Multivendor.E.commerce.dto;

import dev.kayange.Multivendor.E.commerce.entity.users.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data @Builder
public class RoleResponse {
    private Long id;
    private String name;

    public static RoleResponse create(Role role){
        return RoleResponse.builder().id(role.getId()).name(role.getName()).build();
    }
}
