package com.sw300.community.controller;

import com.sw300.community.dto.MemberInformationDto;
import com.sw300.community.model.Member;
import com.sw300.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;


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

    @GetMapping("/searchId")
    public String searchId(){
        return "searchIdForm";
    }

    @GetMapping("/searchPassword")
    public String searchPassword(){
        return "searchPasswordForm";
    }

    @PostMapping("/searchPassword")
    public String changePassword(@RequestParam("email")String email,@RequestParam("name") String name, Model model){
        if(memberService.searchPassword(email,name)){
            model.addAttribute("email",email);
            return "newPasswordForm";
        }
        else
            return "searchPasswordForm";
    }



}
