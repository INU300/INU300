package com.sw300.community.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw300.community.message.model.Message;
import lombok.*;

import javax.validation.constraints.Email;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageDto {

    private Long id;

    private String content;

    private String image;

    private boolean readReceipt;

    private String senderNickname;

    @Email
    private String senderEmail;

    private String receiverNickname;

    @Email
    private String receiverEmail;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp sendDate;

    @Builder
    public MessageDto(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.image = message.getImage();
        this.readReceipt = message.isReadReceipt();
        this.senderNickname = message.getSender().getNickname();
        this.senderEmail = message.getSender().getEmail();
        this.receiverNickname = message.getReceiver().getNickname();
        this.receiverEmail = message.getReceiver().getEmail();
        this.sendDate = message.getSendDate();
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // JSON 변환 중 오류 발생 시 예외 처리
            e.printStackTrace();
            return null; // 또는 다른 오류 처리 로직을 추가
        }
    }
}
