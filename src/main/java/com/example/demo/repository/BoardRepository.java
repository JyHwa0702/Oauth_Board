package com.example.demo.repository;

import com.example.demo.entity.Board;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

    List<Board> findByTitleContaining(String keyword);

    Board findByUser(User user);
}
