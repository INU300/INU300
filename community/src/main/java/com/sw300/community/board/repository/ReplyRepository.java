package com.sw300.community.board.repository;

import com.sw300.community.board.model.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long>{

    @Query("select r from Reply r where r.board.id = :bno and r.deleted = false")
    Page<Reply> listOfBoard(Long bno, Pageable pageable);

//    Page<Reply> findByBoardId(Long bno, Pageable pageable);

}
