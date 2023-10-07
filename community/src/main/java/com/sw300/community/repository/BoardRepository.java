package com.sw300.community.repository;

import com.sw300.community.model.Board;
import com.sw300.community.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    @Query("select b from Board b where b.category.cno = :cno")
    Page<Board> findByCategory(Long cno, Pageable pageable);
}
