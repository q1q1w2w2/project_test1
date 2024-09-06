package com.example.demo.dto;

import com.example.demo.dto.validation.NotEmptyGroup;
import com.example.demo.dto.validation.SizeCheckGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@ToString
public class MemberJoinDto {

    @NotEmpty
//    @Size(min = 2, max = 30)
    private String name;

    @NotEmpty
//    @Size(min = 4, max = 20)
    private String loginId;

    @NotEmpty
//    @Size(min = 8, max = 20)
    private String password;

}
