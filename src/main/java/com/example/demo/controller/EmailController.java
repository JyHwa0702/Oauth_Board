package com.example.demo.controller;

import com.example.demo.dto.EmailDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/emailConfirm")
    public String emailConfirm(@Valid EmailDto emailDto, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "/user/findPwd";
        }


        String email = emailDto.getEmail();
        String authCode = emailService.sendAuthCode(email);

        switch (authCode){
            case "1" :
                model.addAttribute("Message", "인증코드를 해당 메일로 보냈습니다.");
                model.addAttribute("codeConfirm", true);
                model.addAttribute("email", email); // 입력한 email input 히든으로 넘겨줄 예정
                break;

            case "2" :
                model.addAttribute("Message", "해당 이메일 유저가 존재하지 않습니다.");
                break;

            case "3" :
                model.addAttribute("Message","sendAuthCode에서 에러 발생.");
                break;
        }

        return "/user/findPwd";
    }


    @PostMapping("/emailConfirm/code")
    public String confirmEmailCode(String inputEmail,String authKey,Model model) throws ChangeSetPersister.NotFoundException {

        Optional<User> user = emailService.getUser(inputEmail,authKey,model);

        if(user.isPresent()){
            User foundUser = user.get();
            if (foundUser.getEmail().equals(inputEmail)){
                model.addAttribute("email",foundUser);
                return "/user/OkFindPwd";
            }
        }
        return "/user/findPwd";
    }

    @PutMapping("/emailConfirm/code/ok")
    public String changePassword(String email, String password,String password2,Model model){
        Optional<User> byEmail = userRepository.findByEmail(email);

        if (byEmail.isPresent()){ //비밀번호 1,2번 일치로 변경할예정.
            log.info("비밀번호 변경할 비밀번호 : "+password);
            String encodePwd = bCryptPasswordEncoder.encode(password);
            User updateUser = byEmail.get().changePwd(encodePwd);
            userRepository.save(updateUser);
            return "redirect:/user/login";
        } else{
            model.addAttribute("email",email);
            model.addAttribute("message","비밀번호 확인이 일치하지 않습니다!");
            return "/emailConfirm/code/ok";
        }
    }

}

