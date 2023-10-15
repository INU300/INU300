package com.sw300.community.service;

import com.sw300.community.dto.MessageDto;
import com.sw300.community.model.Member;
import com.sw300.community.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Test
    void writeTest() {
        Member sender = Member.builder()
                .nickname("asdfg")
                .email("asdfg@naver.com")
                .build();

        Member receiver = Member.builder()
                .nickname("qwer")
                .email("qwer@naver.com")
                .build();

        Message message = Message.builder()
                .content("message test")
                .sender(sender)
                .receiver(receiver)
                .build();

        MessageDto messageDto = MessageDto.builder().message(message).build();

        /*MessageDto result = messageService.write(messageDto);

        System.out.println("result = " + result);*/
    }
}