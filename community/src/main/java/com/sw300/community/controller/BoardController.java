package com.sw300.community.controller;

import com.sw300.community.model.Reply;
import com.sw300.community.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

//    @GetMapping({"", "/"})
//    public String index(Model model, @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
//        model.addAttribute("boards", boardService.getPostList(pageable));
//        return "index";
//    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String getPost(Model model, @PathVariable Long id) {
        model.addAttribute("boards", boardService.getPost(id));
        Pageable pageable = PageRequest.of(0, 10); // 첫 페이지에서 10개의 댓글을 가져옴

        List<Reply> replyList = boardService.getRepliesByBoardId(id, pageable).getContent();
        model.addAttribute("replies", replyList);
        return "board/detail";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(Model model, @PathVariable int id) {
        model.addAttribute("boards", boardService.getPost(id));
        return "/board/updateForm";
    }

}
