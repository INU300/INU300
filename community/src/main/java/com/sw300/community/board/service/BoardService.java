package com.sw300.community.board.service;

import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.BoardDTO;
import com.sw300.community.board.dto.BoardInput;
import com.sw300.community.board.dto.BoardOutput;
import com.sw300.community.board.enums.LikeStatus;
import com.sw300.community.board.model.Board;
import com.sw300.community.member.dto.MemberCategoryDto;
import com.sw300.community.common.dto.PageRequestDto;
import com.sw300.community.common.dto.PageResponseDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    /**
     * 게시글 추가
     */
    Long register(BoardInput boardInput);

    /**
     * 게시글 조회
     */
    BoardOutput readOne(Long bno);

    /**
     * 게시글 수정
     */
    void modify(BoardOutput boardOutput);

    /**
     * 게시글 삭제
     */
    void remove(Long bno);

    /**
     * 게시글의 조회수 증가
     */
    ServiceResult setBoardHits(Long id, String email);

    /**
     * 게시글의 좋아요 기능
     */
    ServiceResult setBoardLike(Long id, String email, LikeStatus status);

    ServiceResult<Page<Board>> getPopularBoardsToday(Pageable pageable);

    Board getPost(long id);

    PageResponseDto<Board> getPostList(PageRequestDto pageRequestDto);

    PageResponseDto<Board> getFavoriteList(PageRequestDto pageRequestDto, List<MemberCategoryDto> favoriteList);

}

