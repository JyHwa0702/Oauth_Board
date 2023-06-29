package com.example.demo.controller;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.dto.BoardDto;
import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.User;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;


    //댓글 저장
    @PostMapping("/post/{board_id}/comment/write")
    public String saveComment(CommentDto commentDto, Model model, @PathVariable Long board_id
            , @AuthenticationPrincipal PrincipalDetails principalDetails){

        Optional<Board> optionalBoard = boardRepository.findById(board_id);

        if(!optionalBoard.isPresent()){
            log.error("게시물을 찾을 수 없습니다. boardId : {}",board_id);
            return "redirect:/board";
        }


        Board board = optionalBoard.get();
        User user = principalDetails.getUser();
        log.info("CommentController - saveComment : 로그인 유저 email : {}",user.getEmail());
        commentDto.setUser(user);
        commentService.saveComment(commentDto,board);

        if (board.getUser().getId().equals(user.getId())){
            model.addAttribute("writer",true);
            log.info("CommentController - saveComment : writer 로그인");
        }

        log.info("CommentController 내 댓글 저장로직 후");

        return "redirect:/board/post/"+board.getId();
    }

    @PostMapping("/post/comment/delete")
    public String deleteCommentById(Long comment_id,Long board_id){

        commentService.deleteCommentById(comment_id);

        return "redirect:/board/post/"+board_id;
    }
}
