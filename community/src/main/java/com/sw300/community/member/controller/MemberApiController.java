package com.sw300.community.member.controller;

import com.sw300.community.member.dto.MemberSaveDto;
import com.sw300.community.member.model.Member;
import com.sw300.community.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    @PostMapping("/api/join")
    public Long newMem(@Valid @RequestBody MemberSaveDto memberSaveDto){
        return memberService.joinMember(memberSaveDto);
    }

    @PostMapping("/api/changeNickname")
    public Long changeNickname(@RequestBody Map<String,String> newNickname, Principal principal){
        Member member = this.memberService.getMember(principal.getName());
        return memberService.changeNickname(member.getId(),newNickname.get("newNickname"));
    }

    @PostMapping("/api/changePassword")
    public Long changePassword(@RequestBody Map<String,String> password,Principal principal){
        Member member = this.memberService.getMember(principal.getName());
        return memberService.changePassword(member.getId(),password.get("currentPassword"),password.get("newPassword"));
    }

    @PostMapping("/api/newPassword")
    public Long newPassword(@RequestBody Map<String,String> inform){
        System.out.println(inform.get("email"));
        return memberService.newPassword(inform.get("email"),inform.get("newPassword"));
    }


}
