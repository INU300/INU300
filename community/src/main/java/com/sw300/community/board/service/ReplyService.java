package com.sw300.community.board.service;

import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.ReplyInput;
import com.sw300.community.board.enums.LikeStatus;
import com.sw300.community.board.model.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyService {

    /**
     * 댓글 추가
     */
    ServiceResult addReply(ReplyInput replyInput, String email);

    /**
     * 댓글 수정
     */
    ServiceResult updateReply(Long id, ReplyInput replyInput, String email);

    /**
     * 댓글 삭제
     */
    ServiceResult deleteReply(Long id, String email);

    /**
     * 댓글 추천/비추천 기능
     */
    ServiceResult setReplyLike(Long id, String email, LikeStatus status);

    /**
     * 댓글 조회
     */

    ServiceResult<Reply> getReplyById(Long id);

    /**
     * 글의 댓글 목록 조회
     */
    ServiceResult<Page<Reply>> getRepliesByBoardId(Long boardId, Pageable pageable);
}

