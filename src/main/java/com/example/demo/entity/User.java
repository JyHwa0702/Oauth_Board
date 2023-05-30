package com.example.demo.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

//Google Oauth2 로그인 한 사용자들에 대한 정보를 저장하기 위한 User테이블
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class User extends Time{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false,unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<Comment> commentList;

    private String provider; // oauth2를 이용할 경우 어떤 플랫폼을 이용하는지
    private String providerId; // oauth2를 이용할 경우 아이디값.

    @Builder(builderClassName = "UserDetilRegister",builderMethodName = "userDetailRegister")
    public User(String username, String email, String password,Role role,List<Comment> commentList){
        this.commentList = commentList;
        this.username = username;
        this.email = email;
        this.password =password;
        this.role = role;
    }
    @Builder(builderClassName = "OAuth2Register",builderMethodName = "oauth2Register")
    public User(String username, String email, String password,Role role,String provider,String providerId,List<Comment> commentList){
        this.username = username;
        this.email = email;
        this.password =password;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.commentList = commentList;
    }

    public User update(String username){
        this.username = username;
        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    public void changeRole(Role role){
        this.role = role;
    }
    public User changePwd(String password){
        this.password = password;
        return this;
    }
}
