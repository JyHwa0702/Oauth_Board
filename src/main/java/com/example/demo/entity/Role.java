package com.example.demo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//Spring Security 중 사용자의 권한을 Enum 클래스로 만들어 관리한다.
@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_GUEST("ROLE_GUEST","손님"),
    ROLE_USER("ROLE_USER","일반 사용자");


    private final String key;
    private final String title;
}
