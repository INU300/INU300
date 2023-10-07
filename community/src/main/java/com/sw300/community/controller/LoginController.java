package com.sw300.community.controller;

import com.sw300.community.dto.CategoryDto;
import com.sw300.community.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class LoginController {

    private final CategoryService categoryService;

    @GetMapping("/")
    public String mainLogin(){
        return "mainLogin";
    }

    @GetMapping("/join")
    public String join(){
        return "joinPage";
    }

    @GetMapping("/home")
    public void home(Model model){

        List<CategoryDto> dtoList = categoryService.getBestCategory();

        model.addAttribute("dtoList", dtoList);
    }


}
