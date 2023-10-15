package com.sw300.community.controller;

import com.sw300.community.member.model.Member;
import com.sw300.community.category.service.CategoryService;
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
            // TODO 2023.10.14 추후 예외 코드로 변환, 혹은 없애야 함
            member = Member.builder().nickname("(닉네임)").build();
        } else {
            member = memberService.getMember(principal.getName()); // getName() : email
        }

        // TODO 2023.10.14 member 객체를 그대로 보내지 말고 dto를 사용하거나, nickname 문자열만 보내야 함
        model.addAttribute("member", member);
        model.addAttribute("categoryList", categoryService.getAllCategory());
    }
}
