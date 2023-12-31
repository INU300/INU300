package com.sw300.community.board.model;

import com.sw300.community.board.enums.LikeStatus;
import com.sw300.community.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class BoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    private Member member;

    @ManyToOne
    @JoinColumn
    private Board board;

    @Column
    private LocalDateTime regDate;

    @Enumerated(EnumType.STRING)
    private LikeStatus likeStatus; // 좋아요, 싫어요 상태

}
