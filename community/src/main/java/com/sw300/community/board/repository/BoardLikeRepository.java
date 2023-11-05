package com.sw300.community.board.repository;

import com.sw300.community.board.model.Board;
import com.sw300.community.board.model.BoardLike;
import com.sw300.community.board.enums.LikeStatus;
import com.sw300.community.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    long countByBoardAndMember(Board board, Member member);

    Optional<BoardLike> findByBoardAndMember(Board board, Member member);

    int countByBoardIdAndLikeStatus(Long boardId, LikeStatus likeStatus);

}
