package com.sw300.community.controller;

import com.sw300.community.member.model.Member;
import com.sw300.community.service.CategoryService;
import com.sw300.community.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class BaseController {

    private final MemberService memberService;
    private final CategoryService categoryService;

    @ModelAttribute
    public void info(Model model, Principal principal) {
        Member member;
        if (principal == null) {
            member = Member.builder().nickname("(닉네임)").build();
        } else {
            member = memberService.getMember(principal.getName()); // getName() : email
        }
        model.addAttribute("member", member);
        model.addAttribute("categoryList", categoryService.getAllCategory());
    }
}
