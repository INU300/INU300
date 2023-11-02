package com.sw300.community.board.repository;

import com.sw300.community.board.model.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long>{

    Page<Reply> findByBoardId(Long boardId, Pageable pageable);

}
