package com.example.demo1.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class JoinDto {

    @NotBlank
    @Size(min = 2, max = 20)
    private String username;

    @NotBlank
    @Size(min = 3, max = 50)
    private String loginId;

    @NotBlank
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    private LocalDate birth;

    @NotBlank
    private String tel;

    @NotBlank
    private String address;

    @NotBlank
    private String detail;
}
