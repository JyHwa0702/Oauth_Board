package com.example.demo.config.auth;

import com.example.demo.config.auth.userinfo.OAuth2UserInfo;
import com.example.demo.entity.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@ToString
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private OAuth2UserInfo oAuth2UserInfo;

    //UserDetils : Form 로그인시 사용
    public PrincipalDetails(User user){
        this.user = user;
    }



    public PrincipalDetails(User user,OAuth2UserInfo oAuth2UserInfo){
        this.user = user;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    /**
     * UserDetails 구현
     * @return 해당 유저의 권한목록 리턴.
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().toString();
            }
        });
        return collect;
    }

    /**
     * UserDetails 구현
     * @return  비밀번호를 리턴.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    public String getEmail(){
        return user.getEmail();
    }


    public Long getId(){
        return user.getId();
    }

    //계정 만료여부
    //true : 만료안됨
    //false : 만료됨

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //계정 잠김여부
    //true : 잠기지 않음
    //false : 잠김

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //계정 비밀번호 만료 여부
    //true : 만료 안됨
    //false : 만료됨

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //계정 활성화 여부
    //true : 활성화됨
    //false : 활성화 안됨

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserInfo.getAttributes();
    }

    @Override
    public String getName() {
        return oAuth2UserInfo.getProviderId();
    }
}
