package com.sw300.community.board.service;


import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.BoardInput;
import com.sw300.community.board.exception.AlreadyDeletedException;
import com.sw300.community.board.model.Board;
import com.sw300.community.board.model.BoardHits;
import com.sw300.community.board.model.BoardLike;
import com.sw300.community.board.repository.BoardHitsRepository;
import com.sw300.community.board.repository.BoardLikeRepository;
import com.sw300.community.board.repository.BoardRepository;
import com.sw300.community.model.Member;
import com.sw300.community.repository.CategoryRepository;
import com.sw300.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;
    private final BoardHitsRepository boardHitsRepository;
    private final MemberRepository memberRepository;
    private final BoardLikeRepository boardLikeRepository;

    @Override
    public ServiceResult addBoard(BoardInput boardInput, String email) {
        
        Optional<Member> optionalUser = memberRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        Member member = optionalUser.get();

        boardRepository.save(Board.builder()
                .title(boardInput.getTitle())
                .contents(boardInput.getContents())
                // 카테고리 string을 카테고리 repository에서 find name으로 해서 객체 가져오고
                // 여기에 넣기
                .category(boardInput.getCategory())
                .member(boardInput.getMember())
                .regDate(LocalDateTime.now())
                .build());

        return ServiceResult.success();
    }

    @Override
    public ServiceResult updateBoard(Long id, BoardInput boardInput, String email) {

        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }
        Board board = optionalBoard.get();

        Optional<Member> optionalUser = memberRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        Member member = optionalUser.get();

        boardRepository.save(Board.builder()
                .title(boardInput.getTitle())
                .contents(boardInput.getContents())
                .category(boardInput.getCategory())
                .member(boardInput.getMember())
                .updateDate(LocalDateTime.now())
                .build());
        
        return ServiceResult.success();
    }

    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<String> handlerAlreadyDeletedException(AlreadyDeletedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
    }

    @Override
    public ServiceResult deleteBoard(Long id, String email) {

        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }
        Board board = optionalBoard.get();

        Optional<Member> optionalUser = memberRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        Member member = optionalUser.get();

        if (board.isDeleted()) {
            throw new AlreadyDeletedException("이미 삭제된 글입니다.");
        }

        board.setDeleted(true);
        board.setDeletedDate(LocalDateTime.now());

        boardRepository.save(board);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult setBoardHits(Long id, String email) {

        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }
        Board board = optionalBoard.get();

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (!optionalMember.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        if (boardHitsRepository.countByBoardAndMember(board, member) > 0) {
            return ServiceResult.fail("이미 조회수가 반영되었습니다.");
        }

        boardHitsRepository.save(BoardHits.builder()
                .board(board)
                .member(member)
                .regDate(LocalDateTime.now())
                .build());

        return ServiceResult.success();
    }

    @Override
    public ServiceResult setBoardLike(Long id, String email) {

        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }
        Board board = optionalBoard.get();

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (!optionalMember.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        long boardLikeCount = boardLikeRepository.countByBoardAndMember(board, member);
        if (boardLikeCount > 0) {
            return ServiceResult.fail("이미 좋아요한 내용이 있습니다.");
        }

        boardLikeRepository.save(BoardLike.builder()
                .board(board)
                .member(member)
                .regDate(LocalDateTime.now()).build());

        return ServiceResult.success();

    }

    @Override
    public ServiceResult setBoarUnLike(Long id, String email) {

        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }
        Board board = optionalBoard.get();

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (!optionalMember.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        Optional<BoardLike> optionalBoardLike = boardLikeRepository.findByBoardAndMember(board, member);
        if (!optionalBoardLike.isPresent()) {
            return ServiceResult.fail("좋아요한 내용이 없습니다.");
        }
        BoardLike boardLike = optionalBoardLike.get();

        boardLikeRepository.delete(boardLike);

        return ServiceResult.success();
    }
}
