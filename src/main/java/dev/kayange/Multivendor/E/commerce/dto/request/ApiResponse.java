package dev.kayange.Multivendor.E.commerce.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int statusCode;
    private String status;
    private String message;
    private String description;
    private String path;
    private T data;
    private String error;
    private T errors;
}
