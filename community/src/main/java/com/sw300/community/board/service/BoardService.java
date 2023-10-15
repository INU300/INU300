package com.sw300.community.board.service;

import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.BoardInput;

public interface BoardService {

    /**
     * 게시글 추가
     * @param boardInput
     * @param email
     * @return
     */
    ServiceResult addBoard(BoardInput boardInput, String email);

    /**
     * 게시글 수정
     * @param id
     * @param boardInput
     * @param email
     * @return
     */
    ServiceResult updateBoard(Long id, BoardInput boardInput, String email);

    /**
     * 게시글 삭제
     * @param id
     * @param email
     * @return
     */
    ServiceResult deleteBoard(Long id, String email);

    /**
     * 게시글의 조회수 증가
     * @param id
     * @param email
     * @return
     */
    ServiceResult setBoardHits(Long id, String email);

    /**
     * 게시글의 좋아요 기능
     * @param id
     * @param email
     * @return
     */
    ServiceResult setBoardLike(Long id, String email);

    /**
     * 게시글의 좋아요를 취소
     * @param id
     * @param email
     * @return
     */
    ServiceResult setBoarUnLike(Long id, String email);
}
