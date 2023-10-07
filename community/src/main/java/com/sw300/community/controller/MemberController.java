package com.sw300.community.controller;

import com.sw300.community.dto.MemberInformationDto;
import com.sw300.community.model.Member;
import com.sw300.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String mainLogin(){
        return "mainLogin";
    }

    @GetMapping("/join")
    public String join(){
        return "joinPage";
    }

    @GetMapping("/home")
    public String home(){
        return "home";
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
