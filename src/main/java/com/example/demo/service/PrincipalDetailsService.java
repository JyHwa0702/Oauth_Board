package com.example.demo.service;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> byEmail = userRepository.findByEmail(email);
        //optional 변수명 .isempty()하면 비워있으면 true, .isparent()해서 값 있으면 True,
        //.get()하면 해당 변수 들고옴.
        if(byEmail.isPresent()){
            return new PrincipalDetails(byEmail.get());
        }
        return null;
    }
}
