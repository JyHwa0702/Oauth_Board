package com.example.demo.config.auth.userinfo;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{
    private Map<String,Object> attributes;


    public GoogleUserInfo(Map<String,Object> attributes){
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getProvider() {
        return "Google";
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getId(){
        return attributes.get("id").toString();
    }
}
