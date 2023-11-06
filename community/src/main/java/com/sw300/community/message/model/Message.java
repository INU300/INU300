package com.sw300.community.message.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sw300.community.member.model.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="text")
    private String content;

    @Column(columnDefinition="text")
    private String image;

    // TODO 2023.10.15 읽음 여부 추가해야 함
    @ColumnDefault("false")
    private boolean readReceipt;

    @ColumnDefault("false")
    private boolean deletedBySender;

    @ColumnDefault("false")
    private boolean deletedByReceiver;

    @CreationTimestamp
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp sendDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member receiver;

    @Builder
    public Message(String content, String image, Member sender, Member receiver) {
        this.content = content;
        this.image = image;
        this.sender = sender;
        this.receiver = receiver;
    }

    public void deleteBySender() {
        this.deletedBySender = true;
    }

    public void deleteByReceiver() {
        this.deletedByReceiver = true;
    }

    public boolean isDeleted() {
        return isDeletedBySender() && isDeletedByReceiver();
    }

    public void setReadReceipt() { this.readReceipt = true; }
}
