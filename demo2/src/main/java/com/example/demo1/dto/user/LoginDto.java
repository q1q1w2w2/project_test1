package com.example.demo1.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class LoginDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String loginId;

    @NotBlank
    @Size(min = 3, max = 100)
    private String password;
}
