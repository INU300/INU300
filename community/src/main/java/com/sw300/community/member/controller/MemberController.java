package com.sw300.community.member.controller;

import com.sw300.community.board.model.Board;
import com.sw300.community.board.service.BoardService;
import com.sw300.community.category.dto.CategoryDto;
import com.sw300.community.dto.*;
import com.sw300.community.member.dto.MemberCategoryDto;
import com.sw300.community.member.dto.MemberGoodDto;
import com.sw300.community.member.dto.MemberInformationDto;
import com.sw300.community.member.model.Member;
import com.sw300.community.category.service.CategoryService;
import com.sw300.community.member.service.MemberService;
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

    private final BoardService boardService;

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

        PageRequestDto pageRequestDto = PageRequestDto.builder().size(5).build();

        PageResponseDto<Board> favoriteDto = boardService.getFavoriteList(pageRequestDto,favoriteList);

        model.addAttribute("favoriteDto",favoriteDto);

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
                .build();
        List<MemberCategoryDto> favoriteList = memberService.getFavorite(principal.getName());
        if(favoriteList.size()>5) {
            List<MemberCategoryDto> subList = favoriteList.subList(5, favoriteList.size());
            favoriteList.removeAll(subList);
        }
        model.addAttribute("mem",memDto);
        model.addAttribute("favoriteList",favoriteList);
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
