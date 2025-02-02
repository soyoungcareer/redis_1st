package com.cinema.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TooManyRequestsException extends RuntimeException {
    private final HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;

    public TooManyRequestsException(String message) {
        super(message);
    }
}
