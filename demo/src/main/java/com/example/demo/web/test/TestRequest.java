package com.example.demo.web.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class TestRequest<T> {
    private HttpStatus status;
    private String message;
    private T data;
}
