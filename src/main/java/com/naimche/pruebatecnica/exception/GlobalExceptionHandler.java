package com.naimche.pruebatecnica.exception;

import com.naimche.pruebatecnica.dto.ErrorResponseDto;
import com.naimche.pruebatecnica.exception.auth.AuthException;
import com.naimche.pruebatecnica.exception.todo.TodoException;
import com.naimche.pruebatecnica.exception.user.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoException.class)
    public ResponseEntity<ErrorResponseDto> handleTodoException(TodoException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseDto(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthException(AuthException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseDto(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponseDto> handleUserException(UserException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseDto(ex.getCode(), ex.getMessage()));
    }

}