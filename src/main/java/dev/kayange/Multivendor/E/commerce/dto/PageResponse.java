package dev.kayange.Multivendor.E.commerce.dto;

import lombok.*;
import org.springframework.data.domain.Page;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PageResponse<T> {
    private boolean first;
    private int pageNumber;
    private int pageSize;
    private long totalPages;
    private long totalElements;
    private boolean last;
    private T data;

    public static PageResponse<?> create( Page<?> page){
        return PageResponse.builder()
                .totalElements(page.getTotalElements())
                .pageNumber(builder().pageNumber)
                .first(page.isFirst())
                .last(page.isLast())
                .totalPages(page.getTotalPages())
                .pageSize(page.getSize())
                .data(page.toList())
                .build();
    }
}
