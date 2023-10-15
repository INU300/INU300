package com.sw300.community.board.repository;

import com.sw300.community.board.model.Board;
import com.sw300.community.board.model.BoardLike;
import com.sw300.community.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    long countByBoardAndMember(Board board, Member member);

    // 삭제하려면 객체가 있어야 함
    Optional<BoardLike> findByBoardAndMember(Board board, Member member);

}
