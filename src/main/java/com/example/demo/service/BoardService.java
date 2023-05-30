package com.example.demo.service;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@AllArgsConstructor
@Service
public class BoardService {

    private BoardRepository boardRepository;

    private static final int BLOCK_PAGE_NUM_COUNT = 5; // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT = 10; // 한 페이지에 존재하는 게시글 수


    @Transactional
    public List<BoardDto> getBoardlist(Integer pageNum){
        PageRequest pageable = PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by("createdDate").descending());
        Page<Board> page = boardRepository.findAll(pageable);
        List<Board> boards = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for(Board board : boards){
            BoardDto boardDto = new BoardDto(board);
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    public Integer[] getPageList(Integer pageNum){
        PageRequest pageable = PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by("createdDate").descending());
        Page<Board> page = boardRepository.findAll(pageable);


        int totalPages = page.getTotalPages();
        int startPage = Math.max(1, pageNum - 4);
        int endPage = Math.min(totalPages,pageNum+4);

        if(totalPages <=1){
            return new Integer[]{};
        }else {
            List<Integer> pageNumList = new ArrayList<>();
            for (int pageNumber = startPage; pageNum <= endPage; pageNum++) {
                pageNumList.add(pageNumber);
            }

            return pageNumList.toArray(new Integer[0]);

        }


    }

    @Transactional
    public BoardDto getBoard(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        BoardDto boardDto = new BoardDto(board);

        return boardDto;
    }

    @Transactional
    public Long saveBoard(BoardDto boardDto) {

        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public void deletePost(Long id){
        boardRepository.deleteById(id);
    }

    @Transactional
    public List<BoardDto> searchPosts(String keyword){
        List<Board> boards = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();


        for(Board board : boards){
            BoardDto boardDto = new BoardDto(board);
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }
}


