package com.sw300.community.board.repository;

import com.sw300.community.board.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
