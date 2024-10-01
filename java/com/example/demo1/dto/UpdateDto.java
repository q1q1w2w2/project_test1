package com.example.demo1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UpdateDto {

    @NotBlank
    @Size(min = 3, max = 100)
    private String password;

    @NotBlank
    private String tel;

    @NotBlank
    private String address;

    @NotBlank
    private String detail;
}
