package com.sw300.community.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sw300.community.board.common.ResponseMessage;
import com.sw300.community.board.common.ResponseResult;
import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.BoardInput;
import com.sw300.community.board.service.BoardService;
import com.sw300.community.board.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ApiBoardController {

    private final BoardService boardService;

    // 게시글 추가
    @PostMapping("/api/board/")
    public ResponseEntity<?> addBoard(@RequestBody @Valid BoardInput boardInput, 
                                      @RequestHeader("TOKEN") String token) {

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.addBoard(boardInput, email);

        return ResponseResult.result(result);
    }

    // 게시글 수정
    @PutMapping("/api/board/type/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable Long id, @RequestBody @Valid BoardInput boardInput,
                                         @RequestHeader("TOKEN") String token) {

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.updateBoard(id, boardInput, email);

        return ResponseResult.result(result);
    }

    // 게시글 삭제
    @DeleteMapping("/api/board/type/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id, 
                                         @RequestHeader("TOKEN") String token) {

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.deleteBoard(id, email);

        if (!result.isResult()) {
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseResult.result(result);
    }
    
    // 게시글의 조회수 증가
    @PutMapping("/api/board/{id}/hits")
    public ResponseEntity<?> boardHits(@PathVariable Long id,
                                       @RequestHeader("TOKEN") String token) {

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.setBoardHits(id, email);
        if (result.isFail()) {
            return ResponseResult.fail(result.getMessage());
        }

        return ResponseResult.success();
    }

    // 게시글 좋아요 기능
    @PutMapping("/api/board/{id}/like")
    public ResponseEntity<?> boardLike(@PathVariable Long id
            , @RequestHeader("TOKEN") String token) {

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.setBoardLike(id, email);
        return ResponseResult.result(result);

    }

    // 게시글 좋아요 취소 기능
    @PutMapping("/api/board/{id}/unlike")
    public ResponseEntity<?> boardUnLike(@PathVariable Long id
            , @RequestHeader("TOKEN") String token) {

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.setBoarUnLike(id, email);
        return ResponseResult.result(result);

    }
}
