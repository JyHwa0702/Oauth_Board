package com.example.demo.service;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    //댓글 생성
    public void saveComment(CommentDto commentDto,Board board) {

        userRepository.findById(commentDto.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 없습니다. id = " + commentDto.getUser().getId())
        );

        commentDto.setBoard(board);
        Comment comment = commentDto.toEntity();
        commentRepository.save(comment);
    }

    //댓글 삭제
    public void deleteCommentById(Long commentId){
        commentRepository.deleteById(commentId);

    }


}
