package com.sw300.community.board.repository;

import com.sw300.community.board.model.BoardLike;
import com.sw300.community.board.model.Reply;
import com.sw300.community.board.model.ReplyLike;
import com.sw300.community.member.model.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {

    long countByReplyAndMember(Reply reply, Member member);

    Optional<ReplyLike> findByReplyAndMember(Reply reply, Member member);

}
