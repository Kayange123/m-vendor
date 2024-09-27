package dev.kayange.Multivendor.E.commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleUserDto {
    private Long id;
    private String firstName;
    private String surname;
    private String number;
    private String mobile;
}
