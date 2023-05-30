package com.example.demo.entity;

import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

//JPA에서는 프록시 생성을 위해 기본 생성자 하나 있어야한다. -> NOargsConㅡ
//게시판 글 정보들을 모아놓은 Board 테이블

@Setter
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 열필요없을때 보안.
@Table(name = "board")
public class Board extends Time{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(length = 10,nullable = false)
    @Size(min = 2,max = 30)
    private String writer;

    @Column(length = 100,nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false) //
    private String content;

    @Column(columnDefinition = "integer default 0")
    private int view;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE) //EAGER바로 나오기,REMOVE는 같이 지워지기
    @OrderBy("id asc") //댓글정렬
    private List<Comment> comments;


    //Java 디자인 패턴, 생성 시점에 값을 채워준다.
    @Builder
    public Board(Long id,String writer,String title,String content,User user){
        //Assert 구문으로 안전한 객체 생성 패턴을 구현한다.
        Assert.hasText(writer,"writer must not be empty");
        Assert.hasText(title,"title must not be empty");
        Assert.hasText(content,"content must not be empty");

        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.user = user;
    }

}
