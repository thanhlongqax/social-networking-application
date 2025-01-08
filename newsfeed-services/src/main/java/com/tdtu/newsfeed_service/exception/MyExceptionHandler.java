package com.tdtu.newsfeed_service.exception;



import com.tdtu.newsfeed_service.dtos.ResDTO;
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

    @ExceptionHandler({RuntimeException.class, IllegalArgumentException.class})
    public ResponseEntity<?> runtimeExceptionHandler(Exception exception){
        ResDTO<?> response = new ResDTO<>();
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setData(null);
        response.setMessage(exception.getMessage());

        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST)
                .body(response);
    }
}