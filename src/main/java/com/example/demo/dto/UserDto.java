package com.example.demo.dto;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.validator.updateUserValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 2,max = 10,message = "비밀번호를 2~10자 사이로 입력해주세요.")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다.",
                groups = updateUserValidation.class)
    @Size(min = 2,max = 30,message = "이름을 2~30자 사이로 입력해주세요.",
                groups = updateUserValidation.class)
    private String username;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식으로 입력해 주세요;.")
    private String email;

    private String picture;

    private Role role;

    private List<Comment> commentList;
    

    @Transactional
    public UserDto update(String username){
        this.username = username;
        return this;
    }
    public User toEntity(){
        return User.userDetailRegister()
                .username(username)
                .email(email)
                .password(toEntity().getPassword())
                .role(Role.ROLE_USER)
                .build();
    }
}
//@Builder
