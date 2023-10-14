package com.sw300.community.controller;

import com.sw300.community.dto.CategoryDto;
import com.sw300.community.dto.MemberCategoryDto;
import com.sw300.community.dto.MemberGoodDto;
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
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final CategoryService categoryService;

    @GetMapping("/")
    public String mainLogin(){
        return "/member/mainLogin";
    }

    @GetMapping("/join")
    public String join(){
        return "/member/joinPage";
    }

    @GetMapping("/home")
    public void home(Model model, Principal principal){

        List<CategoryDto> dtoList = categoryService.getBestCategory();

        List<MemberCategoryDto> favoriteList = memberService.getFavorite(principal.getName());

        model.addAttribute("favoriteList",favoriteList);

        model.addAttribute("dtoList", dtoList);
    }

    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal){
        Member member = memberService.getMember(principal.getName());

        MemberInformationDto memDto = MemberInformationDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .school(member.getSchool())
                .department(member.getDepartment())
                .subclass(member.getSubclass())
                .favorite(member.getFavorite()).build();
        model.addAttribute("mem",memDto);
        return "/member/myPage";
    }

    @GetMapping("/searchId")
    public String searchId(){
        return "/member/searchIdForm";
    }

    @GetMapping("/searchPassword")
    public String searchPassword(){
        return "/member/searchPasswordForm";
    }

    @PostMapping("/searchPassword")
    public String changePassword(@RequestParam("email")String email,@RequestParam("name") String name, Model model){
        if(memberService.searchPassword(email,name)){
            model.addAttribute("email",email);
            return "/member/newPasswordForm";
        }
        else
            return "/member/searchPasswordForm";
    }

    @GetMapping("/hallOfFame")
    public String hallOfFame(Model model){
        List<MemberGoodDto> goodDto = memberService.getGoodDto();

        model.addAttribute("goodDto",goodDto);

        return "/member/hallOfFame";
    }



}
