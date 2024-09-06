package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@AllArgsConstructor
@ToString
public class MemberLoginDto {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;

}
