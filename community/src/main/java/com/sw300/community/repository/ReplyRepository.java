/**
 * boardService에서 댓글 관련 DB 관련 로직을 호출하는 클래스
 */

package com.sw300.community.repository;

import com.sw300.community.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Page<Reply> findByBoardId(Long boardId, Pageable pageable);
}
