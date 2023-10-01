package com.sw300.community.controller;

import com.sw300.community.dto.PageRequestDto;
import com.sw300.community.dto.PageResponseDto;
import com.sw300.community.model.Board;
import com.sw300.community.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/list")
    public void index(Model model, PageRequestDto pageRequestDto) {

        PageResponseDto<Board> responseDto = boardService.getPostList(pageRequestDto);

        model.addAttribute("responseDto", responseDto);
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
