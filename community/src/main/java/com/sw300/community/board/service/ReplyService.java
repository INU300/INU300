package com.sw300.community.board.service;

import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.PageRequestDTO;
import com.sw300.community.board.dto.PageResponseDTO;
import com.sw300.community.board.dto.ReplyDTO;
import com.sw300.community.board.enums.LikeStatus;

public interface ReplyService {

    /**
     * 댓글 추가
     */
    Long addReply(ReplyDTO replyDTO);

    /**
     * 댓글 수정
     */
    void updateReply(ReplyDTO replyDTO);

    /**
     * 댓글 삭제
     */
    void deleteReply(Long id);

    /**
     * 댓글 조회
     */
    ReplyDTO read(Long rno);

    /**
     * 댓글 목록
     */
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

    /**
     * 댓글 추천/비추천 기능
     */
    ServiceResult setReplyLike(Long id, String email, LikeStatus status);

}

