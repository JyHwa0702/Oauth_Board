package com.example.demo.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    //key를 통해 value리턴
    public String getData(String key){
        ValueOperations<String, String> valueOperation = redisTemplate.opsForValue();
        return valueOperation.get(key);
    }

    //유효시간동안 (key,value) 저장
    public void setDataExpire(String key,String value,long duration){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key,value,expireDuration);
    }

    //Data 삭제
    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}
