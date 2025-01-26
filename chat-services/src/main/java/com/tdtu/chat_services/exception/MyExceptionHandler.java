package com.tdtu.chat_services.exception;


import com.tdtu.chat_services.dto.ResDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<?> missingHeaderRequestHandler(MissingRequestHeaderException ex){
        ResDTO<?> response = new ResDTO<>();
        response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        response.setData(null);
        response.setMessage("Unauthorized: " + ex.getMessage());

        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                .body(response);
    }
}
