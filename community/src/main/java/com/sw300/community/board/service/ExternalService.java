package com.sw300.community.board.service;

import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.BoardDTO;
import com.sw300.community.board.dto.BoardInput;
import com.sw300.community.board.enums.LikeStatus;
import com.sw300.community.board.model.Board;
import com.sw300.community.dto.PageRequestDto;
import com.sw300.community.dto.PageResponseDto;
import com.sw300.community.member.dto.MemberCategoryDto;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface ExternalService {

    /**
     * 카테고리 분류 gpt
     */
    String classifyContent(String title, String contents);


    /**
     * 이미지 생성 dalle
     */
    String generateImage(String prompt);
}

