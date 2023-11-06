package com.sw300.community.board.service;


import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.PageRequestDTO;
import com.sw300.community.board.dto.PageResponseDTO;
import com.sw300.community.board.dto.ReplyDTO;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Long addReply(ReplyDTO replyDTO) {

        Optional<Member> OptionalMember = memberRepository.findByEmail(replyDTO.getReplier());
        Member member = OptionalMember.orElseThrow();

        Optional<Board> optionalBoard = boardRepository.findById(replyDTO.getBno());
        Board board = optionalBoard.orElseThrow();

        Long rno = replyRepository.save(Reply.builder()
                .member(member)
                .board(board)
                .contents(replyDTO.getReplyText())
                .regDate(LocalDateTime.now())
                .build()).getId();

        return rno;
    }

    @Override
    public void updateReply(ReplyDTO replyDTO) {

        Optional<Reply> optionalReply = replyRepository.findById(replyDTO.getRno());
        Reply reply = optionalReply.orElseThrow();

        reply.setContents(replyDTO.getReplyText());
        reply.setUpdateDate(LocalDateTime.now());

        replyRepository.save(reply);

    }

    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<String> handlerAlreadyDeletedException(AlreadyDeletedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
    }

    @Override
    public void deleteReply(Long id) {

        Optional<Reply> optionalReply = replyRepository.findById(id);

        Reply reply = optionalReply.orElseThrow();

        if (reply.isDeleted()) {
            throw new AlreadyDeletedException("이미 삭제된 댓글입니다.");
        }

        reply.setDeleted(true);
        reply.setDeletedDate(LocalDateTime.now());

        replyRepository.save(reply);

    }

    @Override
    public ReplyDTO read(Long rno) {

        Optional<Reply> replyOptional = replyRepository.findById(rno);

        Reply reply = replyOptional.orElseThrow();

        ReplyDTO replyDTO = ReplyDTO.builder()
                .rno(reply.getId())
                .bno(reply.getBoard().getId())
                .replyText(reply.getContents())
                .replier(reply.getMember().getNickname())
                .regDate(reply.getRegDate())
                .modDate(reply.getUpdateDate())
                .deletedDate(reply.getDeletedDate())
                .build();

        return replyDTO;
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <=0? 0: pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("id").ascending());

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        List<ReplyDTO> dtoList =
                result.getContent().stream().map(reply -> {
                            ReplyDTO replyDTO = ReplyDTO.builder()
                                    .rno(reply.getId())
                                    .bno(reply.getBoard().getId())
                                    .replyText(reply.getContents())
                                    .replier(reply.getMember().getNickname())
                                    .regDate(reply.getRegDate())
                                    .modDate(reply.getUpdateDate())
                                    .build();
                            return replyDTO;
                        })
                        .collect(Collectors.toList());

        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
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


}
