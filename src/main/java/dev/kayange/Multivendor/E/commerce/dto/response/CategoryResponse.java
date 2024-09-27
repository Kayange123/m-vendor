package dev.kayange.Multivendor.E.commerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private String icon;

    public static CategoryResponse create(Long id, String name, String description, String icon){
        return CategoryResponse.builder()
                .id(id)
                .name(name)
                .icon(icon)
                .description(description)
                .build();
    }
}
