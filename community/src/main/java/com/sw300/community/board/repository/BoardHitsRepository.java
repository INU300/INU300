package com.sw300.community.board.repository;

import com.sw300.community.board.model.Board;
import com.sw300.community.board.model.BoardHits;
import com.sw300.community.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHitsRepository extends JpaRepository<BoardHits, Long> {

    long countByBoardAndMember(Board board, Member member);

}
