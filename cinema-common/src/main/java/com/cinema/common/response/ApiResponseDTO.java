package com.cinema.common.response;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 공통 API Response
 * */

@Getter
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private HttpStatus status;
    private int customCode;
    private String message;
    private T data;

    public static <T> ApiResponseDTO<T> success(T data, String message) {
        return new ApiResponseDTO<>(HttpStatus.OK, 200, message, data);
    }

    public static <T> ApiResponseDTO<T> error(HttpStatus status, int customCode, String message) {
        return new ApiResponseDTO<>(status, customCode, message, null);
    }
}

