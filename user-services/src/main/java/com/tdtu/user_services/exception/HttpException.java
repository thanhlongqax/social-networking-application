package com.tdtu.user_services.exception;

import com.tdtu.user_services.dto.ResDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public HttpException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public ResDTO<?> toResDTO() {
        return new ResDTO<>(
                status.value(),
                message,
                null
        );
    }
}

