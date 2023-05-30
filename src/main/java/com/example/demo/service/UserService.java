package com.example.demo.service;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User getInfoByEmail(PrincipalDetails principalDetails){
        String userEmail = null;
        if(principalDetails.getUser() != null){
            userEmail = principalDetails.getUser().getEmail();
        }else if (principalDetails.getOAuth2UserInfo() != null){
            userEmail = principalDetails.getOAuth2UserInfo().getEmail();
        }
        Optional<User> byEmail = userRepository.findByEmail(userEmail);
        return byEmail.get();
    }

    @Transactional
    public void updateUser(UserDto userDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User nowUser = principal.getUser();
        User updateUser = nowUser.update(userDto.getUsername());
        userRepository.save(updateUser);
    }

    @Transactional
    public void saveUser(UserDto userDto){
        String encodePwd = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodePwd);
        User user = User.userDetailRegister()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .role(Role.ROLE_USER)
                .password(encodePwd)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void pwdChange(User user,String password){
        String changePwd = passwordEncoder.encode(password);
        user.changePwd(changePwd);
    }

}
