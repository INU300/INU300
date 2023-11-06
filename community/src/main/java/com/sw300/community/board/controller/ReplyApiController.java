package com.sw300.community.board.controller;

import com.sw300.community.board.common.ResponseResult;
import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.PageRequestDTO;
import com.sw300.community.board.dto.PageResponseDTO;
import com.sw300.community.board.dto.ReplyDTO;
import com.sw300.community.board.enums.LikeStatus;
import com.sw300.community.board.service.BoardService;
import com.sw300.community.board.service.ReplyService;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Log4j2
public class ReplyApiController {

    private final BoardService boardService;
    private final ReplyService replyService;


    // 댓글 추가
    @PostMapping(value ="/api/reply", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> addReply(@RequestBody @Valid ReplyDTO replyDTO, Principal principal,
                                     BindingResult bindingResult)throws BindException {

        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        // 작성자 정보
        log.info(principal.getName());
        replyDTO.setReplier(principal.getName());

        log.info(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = replyService.addReply(replyDTO);

        resultMap.put("rno",rno);

        return resultMap;
    }

    // 댓글 수정
    @PutMapping(value = "/api/reply/type/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> updateReply(@PathVariable Long id, @RequestBody ReplyDTO replyDTO) {


        replyDTO.setRno(id); //번호를 일치시킴

        replyService.updateReply(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", id);

        return resultMap;

    }

    // 댓글 삭제
    @PostMapping(value = "/api/reply/type/{id}")
    public Map<String,Long> deleteReply(@PathVariable Long id, Principal principal) {

        replyService.deleteReply(id);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", id);

        return resultMap;
    }

    // 댓글 조회
    @GetMapping("/api/reply/{id}")
    public ReplyDTO getReplyDTO( @PathVariable Long id ){

        ReplyDTO replyDTO = replyService.read(id);

        return replyDTO;
    }

    // 댓글 목록 조회
    @GetMapping(value = "/api/reply/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno,
                                             PageRequestDTO pageRequestDTO){

        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
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
//    @GetMapping("/api/reply/{id}")
//    public ResponseEntity<?> getReply(@PathVariable Long id) {
//        ServiceResult<Reply> result = replyService.getReplyById(id);
//        if (result.isFail()) {
//            return ResponseEntity.badRequest().body(result.getMessage());
//        }
//        return ResponseEntity.ok(result.getData());
//    }

    // 글에 대한 모든 댓글 목록
//    @GetMapping("/api/board/{boardId}/replies")
//    public ResponseEntity<?> getRepliesByBoardId(@PathVariable Long boardId, Pageable pageable) {
//        ServiceResult<Page<Reply>> result = replyService.getRepliesByBoardId(boardId, pageable);
//        if (result.isFail()) {
//            return ResponseEntity.badRequest().body(result.getMessage());
//        }
//        return ResponseEntity.ok(result.getData());
//    }

}
