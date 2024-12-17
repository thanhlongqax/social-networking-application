package com.tdtu.user_services.exception;


import com.tdtu.user_services.dto.respone.ResDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ResDTO<?>> handlerHttpException (HttpException ex){
        return new ResponseEntity<>(ex.toResDTO() , ex.getStatus());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResDTO<?>> handlerGenericException (Exception ex){
        ResDTO<?> res = new ResDTO<>(
                HttpStatus.INSUFFICIENT_STORAGE.value(),
                "An unexpected error occurred: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(res , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
