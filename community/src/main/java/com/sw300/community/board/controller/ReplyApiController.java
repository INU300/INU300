package com.sw300.community.board.controller;

import com.sw300.community.board.common.ResponseMessage;
import com.sw300.community.board.common.ResponseResult;
import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.ReplyInput;
import com.sw300.community.board.enums.LikeStatus;
import com.sw300.community.board.model.Reply;
import com.sw300.community.board.service.BoardService;
import com.sw300.community.board.service.ReplyService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReplyApiController {

    private final BoardService boardService;
    private final ReplyService replyService;


    // 댓글 추가
    @PostMapping("/api/reply")
    public ResponseEntity<?> addReply(@RequestBody @Valid ReplyInput replyInput,
                                      @RequestParam(required = true) String email) {

        if (email == null) {
            return ResponseResult.fail("인증된 사용자가 아닙니다.");
        }

        ServiceResult result = replyService.addReply(replyInput, email);

        return ResponseResult.result(result);
    }

    // 댓글 수정
    @PutMapping("/api/reply/type/{id}")
    public ResponseEntity<?> updateReply(@PathVariable Long id, @RequestBody @Valid ReplyInput replyInput,
                                         @RequestParam(required = true) String email) {

        if (email == null) {
            return ResponseResult.fail("인증된 사용자가 아닙니다.");
        }

        ServiceResult result = replyService.updateReply(id, replyInput, email);

        return ResponseResult.result(result);
    }

    // 댓글 삭제
    @DeleteMapping("/api/reply/type/{id}")
    public ResponseEntity<?> deleteReply(@PathVariable Long id,
                                         @RequestParam(required = true) String email) {

        if (email == null) {
            return ResponseResult.fail("인증된 사용자가 아닙니다.");
        }

        ServiceResult result = replyService.deleteReply(id, email);

        if (!result.isResult()) {
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseResult.result(result);
    }


    // 댓글 추천/비추천 기능
    @PutMapping("/api/reply/{id}/like")
    public ResponseEntity<?> replyLike(@PathVariable Long id,
                                       @RequestParam("status") LikeStatus status,
                                       @RequestParam(required = true) String email) {

        if (email == null) {
            return ResponseResult.fail("인증된 사용자가 아닙니다.");
        }

        ServiceResult result = replyService.setReplyLike(id, email, status);
        return ResponseResult.result(result);
    }

    // 댓글 조회 기능
    @GetMapping("/api/reply/{id}")
    public ResponseEntity<?> getReply(@PathVariable Long id) {
        ServiceResult<Reply> result = replyService.getReplyById(id);
        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }
        return ResponseEntity.ok(result.getData());
    }

    // 글에 대한 모든 댓글 목록
    @GetMapping("/api/board/{boardId}/replies")
    public ResponseEntity<?> getRepliesByBoardId(@PathVariable Long boardId, Pageable pageable) {
        ServiceResult<Page<Reply>> result = replyService.getRepliesByBoardId(boardId, pageable);
        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }
        return ResponseEntity.ok(result.getData());
    }

}
