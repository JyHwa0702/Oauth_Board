package com.example.demo.validator;


import com.example.demo.dto.UserDto;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class UserDtoValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;

        if(userRepository.existsByUsername(userDto.getUsername())){
            errors.rejectValue("username","invalid_username"
                    ,new Object[]{userDto.getUsername()},"이미 사용중인 이름입니다.");
        }

        if(userRepository.existsByEmail(userDto.getEmail())){
            errors.rejectValue("email","invalid_email"
                    ,new Object[]{userDto.getEmail()},"이미 사용중인 이메일입니다.");
        }
    }
}
