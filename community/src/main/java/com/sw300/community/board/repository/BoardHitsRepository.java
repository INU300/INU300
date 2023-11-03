package com.sw300.community.board.repository;

import com.sw300.community.board.model.Board;
import com.sw300.community.board.model.BoardHits;
import com.sw300.community.member.model.Member;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardHitsRepository extends JpaRepository<BoardHits, Long> {

    long countByBoardAndMember(Board board, Member member);

    @Query("SELECT bh.board, COUNT(bh) as hits FROM BoardHits bh WHERE DATE(bh.regDate) = :date GROUP BY bh.board")
    List<Object[]> countHitsByDate(LocalDate date);

}
