package com.example.demo.config.auth;

import com.example.demo.entity.User;
import lombok.Getter;

import java.io.Serializable;



//User 클래가 이미 있는데도 왜 따로 SessionUser 클래스를 생성하는 이유
//-Entity 클래스는 직렬화 코드를 넣지 않는게 좋다.
//-Entity 클래스에는 언제 다른 Entity와 Relationship이 형성될지 모른다.
//-@OneToMany,@ManyToMany등 자식 엔티티를 갖고 있다면, 직렬화 대상에 자식들까지 포함되니 성능이슈, 부수 효과가 발생할 확률이 높다.
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;

    public SessionUser (User user){
        this.name = user.getUsername();
        this.email = user.getEmail();
    }
}
