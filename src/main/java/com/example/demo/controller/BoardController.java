package com.example.demo.controller;
import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.dto.BoardDto;
import com.example.demo.dto.CommentDto;
import com.example.demo.entity.User;
import com.example.demo.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/board")
@Slf4j
public class BoardController {
    private BoardService boardService;


    //게시글 목록 페이지
    @GetMapping({"","/list"})
    public String getBoardlist(Model model, @RequestParam(value = "page",defaultValue = "1")Integer pageNum){
        log.info("board/list getmapping");
        List<BoardDto> boardList =boardService.getBoardlist(pageNum);
        Integer[] pageList = boardService.getPageList(pageNum);

        model.addAttribute("boardList",boardList);
        model.addAttribute("pageList",pageList);

        return "board/list";
    }

    //글쓰는 페이지
    @GetMapping("/post")
    public String getWritePage(Authentication authentication,Model model){
        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        model.addAttribute("userId",user.getId());
        model.addAttribute("boardDto",new BoardDto());
        return "board/write";
    }

    //게시글 작성
    @PostMapping("/post")
    public String savePost(@Valid BoardDto boardDto, BindingResult bindingResult, Authentication authentication){

        //게시판 양식 검사
        if (bindingResult.hasErrors()){
            log.info("board writing PostMapping Errors");
            return "/board/write";
        }

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();
        boardDto.setUser(user);
        boardService.saveBoard(boardDto);

        return "redirect:/board/list";
    }

    //게시물 상세 페이지
    @GetMapping("/post/{no}")
    public String getPost(@PathVariable Long no, Model model, Authentication authentication){
        BoardDto boardDto = boardService.getBoard(no);
        List<CommentDto> comments = boardDto.getComments();

        log.info("해당 게시물 comments들 : "+comments.toString());

        /*댓글 관련*/
        if(!comments.isEmpty()){
            model.addAttribute("comments",comments);

        }

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        if (boardDto.getUser().getId().equals(user.getId())){
            model.addAttribute("writer",true);
        }

        model.addAttribute("boardDto",boardDto);
        return "board/detail";
    }


    //게시물 수정 페이지
    @GetMapping("/post/edit/{no}")
    public String getEditPage(@PathVariable Long no, Model model){
        BoardDto boardDto = boardService.getBoard(no);

        model.addAttribute("boardDto",boardDto);

        return "board/update";
    }


    //게시글 수정
    @PutMapping("/post/edit/{no}")
    public String updatePost(@Valid BoardDto boardDto,BindingResult bindingResult,Authentication authentication){
        if(bindingResult.hasErrors()){
            log.info("board 수정폼에서 Putmapping시 binddingResult 발생");
            return "board/update";
        }

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();
        boardDto.setUser(user);
        boardService.saveBoard(boardDto);

        return "redirect:/board/post/{no}";
    }

    //게시물 삭제
    @DeleteMapping("/post/{no}")
    public String deletePost(@PathVariable Long no){
        boardService.deletePost(no);
        return "redirect:/board/list";
    }

    //검색
    @GetMapping("/search")
    public String searchPosts(@RequestParam String keyword,Model model){
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword);
        model.addAttribute("boardList", boardDtoList);
        return "board/list";
    }

}
