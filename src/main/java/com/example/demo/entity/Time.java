package com.example.demo.entity;

//데이터 조작시 자동으로 날짜를 수정해주는 JPA의 Auditing 기능을 사용
//다른 앤티티로부터 상속받아서 사용

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 클래스가 만들어지지 않는 기초 클래스
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class Time {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedDate;
}
