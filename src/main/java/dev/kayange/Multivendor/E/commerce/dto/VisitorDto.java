package dev.kayange.Multivendor.E.commerce.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitorDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
