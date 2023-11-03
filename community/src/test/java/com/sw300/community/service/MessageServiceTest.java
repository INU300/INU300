package com.sw300.community.service;
import com.sw300.community.member.model.Member;
import com.sw300.community.message.dto.MessageDto;
import com.sw300.community.message.model.Message;
import com.sw300.community.message.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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