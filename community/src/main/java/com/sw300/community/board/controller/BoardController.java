package com.sw300.community.board.controller;

import com.sw300.community.board.dto.BoardDTO;
import com.sw300.community.board.dto.BoardOutput;
import com.sw300.community.board.model.Board;
import com.sw300.community.board.service.BoardService;
import com.sw300.community.common.dto.PageRequestDto;
import com.sw300.community.common.dto.PageResponseDto;
import com.sw300.community.category.service.CategoryService;
import com.sw300.community.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BoardController {

    private final BoardService boardService;

    private final CategoryService categoryService;

    private final MemberService memberService;

    @GetMapping("/board/list")
    public void index(Model model, PageRequestDto pageRequestDto, HttpSession session, Principal principal) {

        // 게시판 조회 시 일별 방문자 수를 증가시킴
        categoryService.incrementDailyVisitors(pageRequestDto.getCno(), session);

        // 개인의 게시판 조회수 상승
        memberService.viewAddCount(pageRequestDto.getCno(), principal.getName());

        PageResponseDto<Board> responseDto = boardService.getPostList(pageRequestDto);

        model.addAttribute("responseDto", responseDto);
    }

    @GetMapping("/board/listing")
    @ResponseBody
    public PageResponseDto<Board> getList(PageRequestDto pageRequestDto) {

        PageResponseDto<Board> responseDto = boardService.getPostList(pageRequestDto);

        return responseDto;
    }

    @GetMapping("/board/result")
    public void result(){
    }

    // 게시글 작성 페이지
    @GetMapping("/board/register")
    public void registerGET(){
    }

    // 게시글 조회
    @GetMapping({"/board/read", "/board/modify"})
    public void read( Long id, Model model){

        BoardOutput boardOutput = boardService.readOne(id);

        log.info(boardOutput);

        model.addAttribute("dto", boardOutput);
    }

}
