package com.sw300.community.controller;

import com.sw300.community.dto.PageRequestDto;
import com.sw300.community.dto.PageResponseDto;
import com.sw300.community.model.Board;
import com.sw300.community.service.BoardService;
import com.sw300.community.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final CategoryService categoryService;

    @GetMapping("/board/list")
    public void index(Model model, PageRequestDto pageRequestDto, HttpSession session) {

        // 게시판 조회 시 일별 방문자 수를 증가시킴
        categoryService.incrementDailyVisitors(pageRequestDto.getCno(), session);

        PageResponseDto<Board> responseDto = boardService.getPostList(pageRequestDto);

        model.addAttribute("responseDto", responseDto);
    }

    @GetMapping("/board/listing")
    @ResponseBody
    public PageResponseDto<Board> getList(PageRequestDto pageRequestDto) {

        PageResponseDto<Board> responseDto = boardService.getPostList(pageRequestDto);

        return responseDto;
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String getPost(Model model, @PathVariable Long id, PageRequestDto pageRequestDto) {
        model.addAttribute("board", boardService.getPost(id));
        return "board/detail";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(Model model, @PathVariable int id) {
        model.addAttribute("boards", boardService.getPost(id));
        return "/board/updateForm";
    }

}
