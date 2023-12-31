package com.sw300.community.board.controller;

import com.sw300.community.board.common.ResponseResult;
import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.BoardInput;
import com.sw300.community.board.dto.BoardOutput;
import com.sw300.community.board.enums.LikeStatus;
import com.sw300.community.board.model.Board;
import com.sw300.community.board.service.BoardService;
import com.sw300.community.board.service.ExternalService;
import com.sw300.community.common.service.EmailService;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;

import com.sw300.community.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@Log4j2
public class BoardApiController {

    private final BoardService boardService;
    private final ExternalService externalService;
    private final EmailService emailService;
    private final MessageService messageService;

    // 이미지 생성 테스트
    @PostMapping("/api/image")
    public ResponseEntity<String> image(@RequestBody Map<String, String> requestData) {
        try {
            String result = externalService.generateImage(requestData.get("title"), requestData.get("contents"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // 폭력성 판단 테스트
    @PostMapping("/api/violence")
    public ResponseEntity<String> violence (@RequestBody Map<String, String> requestData) {
        try {
            String result = externalService.hasViolence(requestData.get("title"), requestData.get("contents"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // 카테고리 분류 테스트
    @PostMapping("/api/category")
    public ResponseEntity<String> category (@RequestBody Map<String, String> requestData) {
        try {
            String result = externalService.classifyContent(requestData.get("title"), requestData.get("contents"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // 위로의 메시지 테스트
    @PostMapping("/api/encouragement")
    public ResponseEntity<String> encouragement (@RequestBody Map<String, String> requestData) {
        try {
            String result = externalService.giveEncouragement(requestData.get("title"), requestData.get("contents"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // 게시글 추가
    @PostMapping("/board/register")
    public String registerPost(@Valid @ModelAttribute BoardInput boardInput, Principal principal,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // valid 에러 처리
        log.info("board POST register.......");

        if (bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/board/register";
        }

        // 작성자 정보
        log.info(principal.getName());
        boardInput.setMember(principal.getName());

        // 폭력성 판단
        String title = boardInput.getTitle();
        String contents = boardInput.getContents();
        String violence = externalService.hasViolence(title, contents);

        String category = "";
        String message = "";
        String imageUrl = "";

        // 유해성 분류 이후
        if (Objects.equals(violence, "1")) {
            category = "쓰레기통";
            message = externalService.giveEncouragement(title, contents);
            imageUrl = externalService.generateImage(title, contents);

            emailService.sendMail(principal.getName(),imageUrl,message);
            messageService.sendComfortMessage(message, imageUrl, principal.getName());

        } else if (Objects.equals(violence, "0")) {
            category = externalService.classifyContent(title, contents);
        }

        boardInput.setCategory(category);

        // boardInput 데이터 이용
        log.info(boardInput);

        Long bno = boardService.register(boardInput);

        String result = String.format("카테고리 분석 결과는 %s", category);
        redirectAttributes.addFlashAttribute("result", result);

        return "redirect:/board/result";
    }

    // 게시글 수정
    @PostMapping("/board/modify")
    public String modify(BoardOutput boardOutput,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.info("board modify post......." + boardOutput);

        if (bindingResult.hasErrors()) {
            log.info("has errors.......");

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("id", boardOutput.getId());

            return "redirect:/board/modify";
        }

        // 폭력성 판단
        String title = boardOutput.getTitle();
        String contents = boardOutput.getContents();
        String category = "";
        String violence = externalService.hasViolence(title, contents);

        // 카테고리 분류
        if (Objects.equals(violence, "1")) {
            category = "쓰레기통";
        } else if (Objects.equals(violence, "0")) {
            category = externalService.classifyContent(title, contents);
        }
        boardOutput.setCategory(category);

        log.info(boardOutput);

        boardService.modify(boardOutput);

        redirectAttributes.addFlashAttribute("result", "수정되었습니다.");
        redirectAttributes.addAttribute("id", boardOutput.getId());

        return "redirect:/board/read";
    }

    // 게시글 삭제
    @PostMapping("/board/remove")
    public String remove(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {

        log.info("remove post.. " + id);

        boardService.remove(id);

        redirectAttributes.addFlashAttribute("result", "삭제되었습니다.");

        return "redirect:/board/result";

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

    // 글 추천/비추천 기능
    @PutMapping("/api/board/{id}/like")
    public ResponseEntity<?> boardLike(@PathVariable Long id,
                                       @RequestParam("status") LikeStatus status,
                                       @RequestParam(required = true) String email) {

        ServiceResult result = boardService.setBoardLike(id, email, status);
        return ResponseResult.result(result);
    }


}
