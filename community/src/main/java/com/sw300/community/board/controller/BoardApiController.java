package com.sw300.community.board.controller;

import com.sw300.community.board.common.ResponseMessage;
import com.sw300.community.board.common.ResponseResult;
import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.BoardInput;
import com.sw300.community.board.model.Board;
import com.sw300.community.board.service.BoardService;
import com.sw300.community.member.model.Member;
import com.sw300.community.member.service.MemberService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
public class BoardApiController {

    private final BoardService boardService;

    // 게시글 추가
    @PostMapping("/api/board")
    public ResponseEntity<?> addBoard(@RequestBody @Valid BoardInput boardInput,
                                      Principal principal) {

        if (principal == null) {
            return ResponseResult.fail("인증된 사용자가 아닙니다.");
        }
        String email = principal.getName();

        ServiceResult result = boardService.addBoard(boardInput, email);

        return ResponseResult.result(result);
    }

    // 게시글 수정
    @PutMapping("/api/board/type/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable Long id, @RequestBody @Valid BoardInput boardInput,
                                         Principal principal) {

        if (principal == null) {
            return ResponseResult.fail("인증된 사용자가 아닙니다.");
        }

        String email = principal.getName();

        ServiceResult result = boardService.updateBoard(id, boardInput, email);

        return ResponseResult.result(result);
    }

    // 게시글 삭제
    @DeleteMapping("/api/board/type/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id,
                                         Principal principal) {

        if (principal == null) {
            return ResponseResult.fail("인증된 사용자가 아닙니다.");
        }

        String email = principal.getName();

        ServiceResult result = boardService.deleteBoard(id, email);

        if (!result.isResult()) {
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseResult.result(result);
    }
    
    // 게시글의 조회수 증가
    @PutMapping("/api/board/{id}/hits")
    public ResponseEntity<?> boardHits(@PathVariable Long id,
                                       Principal principal) {

        if (principal == null) {
            return ResponseResult.fail("인증된 사용자가 아닙니다.");
        }

        String email = principal.getName();

        ServiceResult result = boardService.setBoardHits(id, email);
        if (result.isFail()) {
            return ResponseResult.fail(result.getMessage());
        }

        return ResponseResult.success();
    }

    @GetMapping("/api/board/popular/today")
    public ResponseEntity<?> getPopularBoardsToday(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("hits").descending());
        ServiceResult<Page<Board>> result = boardService.getPopularBoardsToday(pageable);

        if (result.isFail()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(result.getMessage());
        }

        return ResponseEntity.ok(result.getData());
    }

    // 게시글 좋아요 기능
    @PutMapping("/api/board/{id}/like")
    public ResponseEntity<?> boardLike(@PathVariable Long id
            , Principal principal) {

        if (principal == null) {
            return ResponseResult.fail("인증된 사용자가 아닙니다.");
        }

        String email = principal.getName();

        ServiceResult result = boardService.setBoardLike(id, email);
        return ResponseResult.result(result);

    }


}
