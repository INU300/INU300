package com.sw300.community.controller;

import com.sw300.community.dto.CategoryDto;
import com.sw300.community.dto.MemberInformationDto;
import com.sw300.community.model.Member;
import com.sw300.community.service.CategoryService;
import com.sw300.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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

    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal){
        Member member = this.memberService.getMember(principal.getName());
        MemberInformationDto memDto = new MemberInformationDto(member.getEmail(),member.getName(),member.getNickname(),member.getSchool(),member.getDepartment(),
                member.getSubclass(),member.getBookmark());
        model.addAttribute("mem",memDto);
        return "myPage";
    }




}
