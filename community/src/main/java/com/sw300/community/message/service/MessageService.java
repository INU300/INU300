package com.sw300.community.message.service;

import com.sw300.community.message.dto.MessageDto;
import com.sw300.community.member.model.Member;
import com.sw300.community.member.repository.MemberRepository;
import com.sw300.community.message.model.Message;
import com.sw300.community.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRepository messageRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void write(MessageDto messageDto) {

        Member sender = memberRepository.findByEmail(messageDto.getSenderEmail()).orElseThrow();
        Member receiver = memberRepository.findByEmail(messageDto.getReceiverEmail()).orElseThrow();

        Message message = Message.builder()
                .content(messageDto.getContent())
                .sender(sender)
                .receiver(receiver)
                .build();

        messageRepository.save(message);
    }

    public List<MessageDto> getReceivedMessage(Member member) {

        // TODO 2023.10.14 messageRepository 페이징 처리 필요
        List<Message> messageList = messageRepository.findAllByReceiverOrderByIdDesc(member);

        List<MessageDto> messageDtoList = messageList.stream()
                .filter(message -> !message.isDeletedByReceiver())  // 수신자가 삭제하지 않은 쪽지만
                .map(message -> MessageDto.builder().message(message).build())
                .collect(Collectors.toList());

        return messageDtoList;
    }

    public List<MessageDto> getSentMessage(Member member) {

        List<Message> messageList = messageRepository.findAllBySenderOrderByIdDesc(member);

        List<MessageDto> messageDtoList = messageList.stream()
                .filter(message -> !message.isDeletedBySender())  // 수신자가 삭제하지 않은 쪽지만
                .map(message -> MessageDto.builder().message(message).build())
                .collect(Collectors.toList());

        return messageDtoList;
    }

    @Transactional
    public Object deleteMessageByReceiver(Long id, Member member) {

        Message message = messageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("쪽지를 찾을 수 없습니다."));

        if (member == message.getReceiver()) {

            message.deleteByReceiver(); // 수신자에게 쪽지 삭제
            if (message.isDeleted()) {
                // 받은 사람과 보낸 사람 모두 삭제 했으면, 데이터베이스에서 삭제 요청

                messageRepository.delete(message);
                return "양쪽 모두 삭제";
            }
            return "한쪽만 삭제";
        } else {
            return new IllegalArgumentException("유저 정보가 일치하지 않습니다");
        }
    }

    @Transactional
    public Object deleteMessageBySender(Long id, Member member) {

        Message message = messageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("쪽지를 찾을 수 없습니다."));

        if (member == message.getSender()) {

            message.deleteBySender(); // 송신자에게 쪽지 삭제
            if (message.isDeleted()) {
                // 받은 사람과 보낸 사람 모두 삭제 했으면, 데이터베이스에서 삭제 요청

                messageRepository.delete(message);
                return "양쪽 모두 삭제";
            }
            return "한쪽만 삭제";
        } else {
            return new IllegalArgumentException("유저 정보가 일치하지 않습니다");
        }
    }

    @Transactional
    public Object setReadReceipt(Long id, Member member) {

        Message message = messageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("쪽지를 찾을 수 없습니다."));

        if (member == message.getReceiver()) {

            message.setReadReceipt(); // 쪽지를 읽음으로 변경
            messageRepository.save(message);

            return "쪽지 읽음 처리";
        } else {
            return new IllegalArgumentException("유저 정보가 일치하지 않습니다");
        }
    }


    @Transactional
    public Object sendComfortMessage (String content, String url, String receiverEmail) { // 글 작성 후 위로 이미지 전송

        Optional<Member> sender = memberRepository.findByEmail("admin@community.com");
        if (sender.isEmpty()) return "위로 글 전송 실패";
        Member receiver = memberRepository.findByEmail(receiverEmail).orElseThrow();

        Message message = Message.builder()
                .content(content)
                .image(url)
                .sender(sender.get())
                .receiver(receiver)
                .build();

        message.deleteBySender();

        messageRepository.save(message);

        return "위로 글 전송";
    }

    public MessageDto getComfortMessage(String receiverEmail) {

        Optional<Member> sender = memberRepository.findByEmail("admin@community.com");
        if (sender.isEmpty()) return null;

        Member receiver = memberRepository.findByEmail(receiverEmail).orElseThrow();

        List<Message> messageList = messageRepository.findBySenderAndReceiver(sender.get(), receiver);

        MessageDto messageDto = null;

        if (!messageList.isEmpty()) {
            for (Message message : messageList) {
                if (!message.isReadReceipt()) {
                    messageDto = MessageDto.builder().message(message).build();
                    break;
                }
            }
        }

        return messageDto;
    }
}
