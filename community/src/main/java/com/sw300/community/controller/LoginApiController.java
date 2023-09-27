package com.sw300.community.controller;

import com.sw300.community.dto.MemberSaveDto;
import com.sw300.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class LoginApiController {

    private final MemberService memberService;
    @PostMapping("/api/join")
    public Long newMem(@Valid @RequestBody MemberSaveDto memberSaveDto){
        return memberService.joinMember(memberSaveDto);
    }
}
