package com.example.demo.exception.exhandler;


import com.example.demo.exception.UserExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> IllegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.error("[IllegalArgumentException 발생]");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<String> UserExistExceptionHandler(UserExistException e) {
        log.error("[UserExistException 발생]");
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> AuthenticationExceptionHandler(AuthenticationException e) {
        log.error("[AuthenticationException 발생]");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("아이디 또는 비밀번호가 틀렸습니다.");
    }
}