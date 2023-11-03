package com.sw300.community.board.repository;

import com.sw300.community.board.model.Board;
import java.time.LocalDate;
import com.sw300.community.board.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    @Query("select b from Board b where b.category.cno = :cno")
    Page<Board> findByCategory(Long cno, Pageable pageable);

    @Query("SELECT b, COUNT(bh) as hitCount FROM Board b LEFT JOIN BoardHits bh ON b.id = bh.board.id WHERE DATE(b.regDate) = :today GROUP BY b.id ORDER BY hitCount DESC")
    Page<Board> findPopularBoardsByDate(LocalDate today, Pageable pageable);

}
