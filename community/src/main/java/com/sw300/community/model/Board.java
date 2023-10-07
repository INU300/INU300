package com.sw300.community.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @ManyToOne(fetch = FetchType.LAZY) // Many = Board, One = User
//    @JoinColumn(name = "userId")
//    private Uers user;

    @Column(nullable = false, length = 100)
    private String title;

    private String content;

/*    @Column(nullable = false, length = 20)
    private String category;
    -> Category로 변경
*/

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private int upVotes;   // 추천 수
    private int downVotes; // 비추천 수

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id desc")
    private List<Reply> replyList;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;

}
