package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class MemberLoginDto {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;

}
