package com.sw300.community.controller;

import com.sw300.community.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.sw300.community.dto.PageRequestDto;
import com.sw300.community.dto.PageResponseDto;
import com.sw300.community.model.Board;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/list")
    public void index(Model model, PageRequestDto pageRequestDto, @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        PageResponseDto<Board> responseDto = boardService.getPostList(pageRequestDto);

        System.out.println("/board/list:" + responseDto.getDtoList().stream().findAny());

        model.addAttribute("responseDto", responseDto);

        //model.addAttribute("boards", boardService.getPostList(pageable));
        //return "home";

//    @GetMapping({"", "/"})
//    public String index(Model model, @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
//        model.addAttribute("boards", boardService.getPostList(pageable));
//        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String getPost(Model model, @PathVariable int id) {
        model.addAttribute("boards", boardService.getPost(id));
        return "board/detail";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(Model model, @PathVariable int id) {
        model.addAttribute("boards", boardService.getPost(id));
        return "/board/updateForm";
    }

}
