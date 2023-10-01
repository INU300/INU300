/**
 * 댓글에 필요한 데이터 필드를 정의한 클래스
 */

package com.sw300.community.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity // row
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // Many = Reply, One = Board
    @JoinColumn(name = "boardId")
    private Board board;

//    @ManyToOne(fetch = FetchType.LAZY) // Many = Reply, One = User
//    @JoinColumn(name = "userId")
//    private User user;

    @CreationTimestamp
    private LocalDateTime createDate;
}
