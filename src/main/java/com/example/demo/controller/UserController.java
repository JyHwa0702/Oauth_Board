package com.example.demo.controller;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.dto.EmailDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.validator.UserDtoValidator;
import com.example.demo.validator.updateUserValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserDtoValidator userDtoValidator;
    private final UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user/join";
    }

    @PostMapping("/join")
    public String join(@Validated UserDto userDto, BindingResult bindingResult, Model model) {

        //유저 양식 검사
        if (bindingResult.hasErrors()) {
            return "user/join";
        }
        //중복검사
        userDtoValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "user/join";
        }

        try {
            userService.saveUser(userDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/join";
        }
        return "redirect:/user/login";
    }

    @PutMapping("/update")
    public String update(@Validated(updateUserValidation.class) UserDto userDto, BindingResult bindingResult) {

        //유저 양식 검사
        if (bindingResult.hasFieldErrors()) {
            return "user/update";
        }
        userService.updateUser(userDto);
        return "index";
    }

    @GetMapping("/login/fail")
    public String loginFailForm() {
        return "user/loginFail";
    }

    @GetMapping("/update")
    public String updateForm(Authentication authentication, Model model) {

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = userService.getInfoByEmail(principal);
        model.addAttribute("userDto", user);
        return "user/update";
    }

    @GetMapping("/findPwd")
    public String findPwd(Model model) {
        model.addAttribute("emailDto", new EmailDto());
        model.addAttribute("codeConfirm",false);
        return "/user/findPwd";
    }
}