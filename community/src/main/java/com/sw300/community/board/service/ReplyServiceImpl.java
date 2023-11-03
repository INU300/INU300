package com.sw300.community.board.service;


import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.ReplyInput;
import com.sw300.community.board.enums.LikeStatus;
import com.sw300.community.board.exception.AlreadyDeletedException;
import com.sw300.community.board.model.Board;
import com.sw300.community.board.model.Reply;
import com.sw300.community.board.model.ReplyLike;
import com.sw300.community.board.repository.BoardRepository;
import com.sw300.community.board.repository.ReplyLikeRepository;
import com.sw300.community.board.repository.ReplyRepository;
import com.sw300.community.member.model.Member;
import com.sw300.community.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
@Service
public class ReplyServiceImpl implements ReplyService {

    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final ReplyLikeRepository replyLikeRepository;

    @Override
    public ServiceResult addReply(ReplyInput replyInput, String email) {

        Optional<Member> optionalUser = memberRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        Member member = optionalUser.get();

        Optional<Board> optionalBoard = boardRepository.findById(replyInput.getBoardId());
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }
        Board board = optionalBoard.get();

        replyRepository.save(Reply.builder()
                .member(member)
                .board(board)
                .contents(replyInput.getContents())
                .regDate(LocalDateTime.now())
                .build());

        return ServiceResult.success();
    }

    @Override
    public ServiceResult updateReply(Long id, ReplyInput replyInput, String email) {

        Optional<Reply> optionalReply = replyRepository.findById(id);
        if (!optionalReply.isPresent()) {
            return ServiceResult.fail("댓글이 존재하지 않습니다.");
        }
        Reply reply = optionalReply.get();

        Optional<Member> optionalUser = memberRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        Member member = optionalUser.get();

        Optional<Board> optionalBoard = boardRepository.findById(replyInput.getBoardId());
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }
        Board board = optionalBoard.get();

        reply.setContents(replyInput.getContents());
        reply.setUpdateDate(LocalDateTime.now());

        replyRepository.save(reply);

        return ServiceResult.success();
    }

    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<String> handlerAlreadyDeletedException(AlreadyDeletedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
    }

    @Override
    public ServiceResult deleteReply(Long id, String email) {

        Optional<Reply> optionalReply = replyRepository.findById(id);
        if (!optionalReply.isPresent()) {
            return ServiceResult.fail("댓글이 존재하지 않습니다.");
        }
        Reply reply = optionalReply.get();

        Optional<Member> optionalUser = memberRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        Member member = optionalUser.get();

        if (reply.isDeleted()) {
            throw new AlreadyDeletedException("이미 삭제된 댓글입니다.");
        }

        reply.setDeleted(true);
        reply.setDeletedDate(LocalDateTime.now());

        replyRepository.save(reply);

        return ServiceResult.success();

    }

    @Override
    public ServiceResult setReplyLike(Long id, String email, LikeStatus status) {

        Optional<Reply> optionalReply = replyRepository.findById(id);
        if (!optionalReply.isPresent()) {
            return ServiceResult.fail("댓글이 존재하지 않습니다.");
        }
        Reply reply = optionalReply.get();

        Optional<Member> optionalUser = memberRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        Member member = optionalUser.get();

        Optional<ReplyLike> existingLike = replyLikeRepository.findByReplyAndMember(reply, member);

        if (existingLike.isPresent()) {
            // 사용자가 이미 좋아요/싫어요를 한 경우 상태 변경 또는 취소
            ReplyLike replyLike = existingLike.get();
            if (status == LikeStatus.NONE) {
                // 상태를 취소하는 경우
                replyLikeRepository.delete(replyLike);
            } else {
                // 상태를 변경하는 경우
                replyLike.setLikeStatus(status);
                replyLikeRepository.save(replyLike);
            }
        } else if (status != LikeStatus.NONE) {
            // 새로운 좋아요/싫어요를 추가하는 경우
            replyLikeRepository.save(ReplyLike.builder()
                    .member(member)
                    .reply(reply)
                    .regDate(LocalDateTime.now())
                    .likeStatus(status)
                    .build());
        }

        return ServiceResult.success();
    }

    @Override
    public ServiceResult<Reply> getReplyById(Long id) {

        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply == null) {
            return ServiceResult.fail("댓글을 찾을 수 없습니다.");
        }
        return ServiceResult.success(reply);

    }

    @Override
    public ServiceResult<Page<Reply>> getRepliesByBoardId(Long boardId, Pageable pageable) {

        Page<Reply> replies = replyRepository.findByBoardId(boardId, pageable);

        if (replies.isEmpty()) {
            return ServiceResult.fail("해당 게시글에는 댓글이 없습니다.");
        }
        return ServiceResult.success(replies);

    }

}
