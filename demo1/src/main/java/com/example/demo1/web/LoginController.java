package com.example.demo1.web;

import com.example.demo1.dto.LoginDto;
import com.example.demo1.exception.CustomValidationException;
import com.example.demo1.jwt.TokenProvider;
import com.example.demo1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final TokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@Validated @RequestBody LoginDto dto, BindingResult bindingResult) {
        log.info("** login post **");
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors: ");
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage());
            });
            throw new CustomValidationException(errorMessage.toString());
        }

        return ResponseEntity.ok().body("ok");
    }
}
