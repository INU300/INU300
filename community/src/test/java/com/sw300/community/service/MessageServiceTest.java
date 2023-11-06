package com.sw300.community.service;
import com.sw300.community.member.model.Member;
import com.sw300.community.message.dto.MessageDto;
import com.sw300.community.message.model.Message;
import com.sw300.community.message.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
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

    @Test
    @Commit
    void sendComfortMessageTest(){
        messageService.sendComfortMessage("위로 글 테스트입니다", "https://lifet-img.s3.ap-northeast-2.amazonaws.com/6b980705-1d57-46a4-8193-ca490d19d00d", "asdfg@naver.com");
    }
}