package com.sw300.community.controller;

import com.sw300.community.dto.MessageDto;
import com.sw300.community.model.Member;
import com.sw300.community.service.MemberService;
import com.sw300.community.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    private final MemberService memberService;

    @GetMapping("/message/send")
    public void sendForm() {}

    @GetMapping("/message/list")
    public void getMessageList(Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());
//        Member member = memberService.getMember("asdfg@naver.com");

        List<MessageDto> receivedMessageDtos = messageService.receivedMessage(member);
        List<MessageDto> sentMessageDtos = messageService.sentMessage(member);

        model.addAttribute("receivedMessageDtos", receivedMessageDtos);
        model.addAttribute("sentMessageDtos", sentMessageDtos);
    }

}
