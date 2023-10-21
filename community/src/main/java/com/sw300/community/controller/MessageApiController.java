package com.sw300.community.controller;

import com.sw300.community.dto.MessageDto;
import com.sw300.community.dto.ResponseDto;
import com.sw300.community.member.model.Member;
import com.sw300.community.member.service.MemberService;
import com.sw300.community.service.MessageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MessageApiController {

    private final MessageService messageService;

    private final MemberService memberService;

    @ApiOperation(value = "쪽지 보내기", notes = "해당하는 유저에게 쪽지를 보낸다")
    @PostMapping("api/message")
    public ResponseDto<?> sendMessage(@RequestBody MessageDto messageDto, Principal principal) {

        // TODO 2023.10.14 BaseController에서 model로 보낸 member를 사용해서 form에 같이 넣어도 되지만 receiver 쪽이 깔끔하지 않을 것 같음.
        // messageDto에 Member를 넣어놔도 되지 않나
        //Member member = memberService.getMember(principal.getName());

        //messageDto.setSenderEmail(member.getEmail()); //

        messageService.write(messageDto);

        return new ResponseDto<>(HttpStatus.CREATED, "쪽지를 성공적으로 보냈습니다");
    }

    @ApiOperation(value = "수신 쪽지 삭제", notes = "선택한 수신 쪽지를 삭제합니다")
    @PostMapping("api/message/received/{id}")
    public ResponseDto<?> deleteReceivedMessage(@PathVariable("id") Long id, Principal principal) {

        Member member = memberService.getMember(principal.getName());

        messageService.deleteMessageByReceiver(id, member);

        return new ResponseDto<>(HttpStatus.NO_CONTENT, "쪽지를 성공적으로 삭제했습니다");
    }

    @ApiOperation(value = "송신 쪽지 삭제", notes = "선택한 송신 쪽지를 삭제합니다")
    @PostMapping("api/message/sent/{id}")
    public ResponseDto<?> deleteSentMessage(@PathVariable("id") Long id, Principal principal) {

        Member member = memberService.getMember(principal.getName());

        messageService.deleteMessageBySender(id, member);

        return new ResponseDto<>(HttpStatus.NO_CONTENT, "쪽지를 성공적으로 삭제했습니다");
    }
}
