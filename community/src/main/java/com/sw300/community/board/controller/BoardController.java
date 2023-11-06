package com.sw300.community.board.controller;

import com.sw300.community.board.dto.BoardOutput;
import com.sw300.community.board.enums.LikeStatus;
import com.sw300.community.board.model.Board;
import com.sw300.community.board.repository.BoardLikeRepository;
import com.sw300.community.board.service.BoardService;
import com.sw300.community.category.service.CategoryService;
import com.sw300.community.common.dto.PageRequestDto;
import com.sw300.community.common.dto.PageResponseDto;
import com.sw300.community.member.model.Member;
import com.sw300.community.member.repository.MemberRepository;
import com.sw300.community.member.service.MemberService;
import com.sw300.community.message.dto.MessageDto;
import com.sw300.community.message.service.MessageService;
import java.security.Principal;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BoardController {

    private final BoardService boardService;
    private final CategoryService categoryService;
    private final MemberService memberService;

    private final MessageService messageService;

    private final MemberRepository memberRepository;
    private final BoardLikeRepository boardLikeRepository;

    @GetMapping("/board/list")
    public void index(Model model, PageRequestDto pageRequestDto, HttpSession session, Principal principal) {

        // 게시판 조회 시 일별 방문자 수를 증가시킴
        categoryService.incrementDailyVisitors(pageRequestDto.getCno(), session);

        // 개인의 게시판 조회수 상승
        memberService.viewAddCount(pageRequestDto.getCno(), principal.getName());

        PageResponseDto<Board> responseDto = boardService.getPostList(pageRequestDto);

        model.addAttribute("responseDto", responseDto);

        MessageDto messageDto = messageService.getComfortMessage(principal.getName());

        // 위로 이미지 코드 "https://lifet-img.s3.ap-northeast-2.amazonaws.com/6b980705-1d57-46a4-8193-ca490d19d00d"
        model.addAttribute("messageDto", messageDto);
    }

    @GetMapping("/board/listing")
    @ResponseBody
    public PageResponseDto<Board> getList(PageRequestDto pageRequestDto) {

        PageResponseDto<Board> responseDto = boardService.getPostList(pageRequestDto);

        return responseDto;
    }

    @GetMapping("/board/result")
    public void result(){
    }

    // 게시글 작성 페이지
    @GetMapping("/board/register")
    public void registerGET(){
    }

    // 게시글 조회
    @GetMapping({"/board/read", "/board/modify"})
    public void read(@RequestParam Long id, Model model, Principal principal){

        BoardOutput boardOutput = boardService.readOne(id);
        log.info(boardOutput);

        // 글 작성자 정보
        Optional<Member> optionalMember = memberRepository.findByNickname(boardOutput.getMember());
        Member member = optionalMember.orElseThrow();

        // 로그인한 사람 정보
        String email = principal.getName();
        Optional<Member> optionalUser = memberRepository.findByEmail(email);
        Member user = optionalUser.orElseThrow();
        String nickname = user.getNickname();

        // 작성자 여부 판단
        boolean isAuthor = principal != null && email.equals(member.getEmail());

        // 추천 비추천
        int likeCount = boardLikeRepository.countByBoardIdAndLikeStatus(id, LikeStatus.LIKE);
        int dislikeCount = boardLikeRepository.countByBoardIdAndLikeStatus(id, LikeStatus.DISLIKE);

        model.addAttribute("dto", boardOutput);
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("user", email);
        model.addAttribute("userNick", nickname);

        model.addAttribute("likeCount", likeCount);
        model.addAttribute("dislikeCount", dislikeCount);

    }

}
