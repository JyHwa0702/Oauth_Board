package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class EmailDto {

    @Email
    @NotEmpty(message = "이메일을 입력해주세요.")
    public String email;

    public String authKey;

}
