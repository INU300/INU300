package com.sw300.community.board.service;


import com.sw300.community.board.common.ServiceResult;
import com.sw300.community.board.dto.BoardInput;
import com.sw300.community.board.dto.BoardOutput;
import com.sw300.community.board.enums.LikeStatus;
import com.sw300.community.board.model.Board;
import com.sw300.community.board.model.BoardHits;
import com.sw300.community.board.model.BoardLike;
import com.sw300.community.board.repository.BoardHitsRepository;
import com.sw300.community.board.repository.BoardLikeRepository;
import com.sw300.community.board.repository.BoardRepository;
import com.sw300.community.category.model.Category;
import com.sw300.community.category.repository.CategoryRepository;
import com.sw300.community.common.dto.PageRequestDto;
import com.sw300.community.common.dto.PageResponseDto;
import com.sw300.community.member.dto.MemberCategoryDto;
import com.sw300.community.member.model.Member;
import com.sw300.community.member.repository.MemberRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;
    private final BoardHitsRepository boardHitsRepository;
    private final MemberRepository memberRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final ModelMapper modelMapper;


    @Override
    public Long register(BoardInput boardInput) {

        Optional<Member> OptionalMember = memberRepository.findByEmail(boardInput.getMember());
        Member member = OptionalMember.orElseThrow();

        Optional<Category> OptionalCategory = categoryRepository.findByName(boardInput.getCategory());
        Category category = OptionalCategory.orElseThrow();

        return  boardRepository.save(Board.builder()
                .title(boardInput.getTitle())
                .contents(boardInput.getContents())
                .member(member)
                .category(category)
                .regDate(LocalDateTime.now())
                .build()).getId();
    }

    @Override
    public BoardOutput readOne(Long bno) {

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        BoardOutput boardOutput = BoardOutput.builder()
                .id(board.getId())
                .title(board.getTitle())
                .contents(board.getContents())
                .category(board.getCategory().getName())
                .member(board.getMember().getNickname())
                .regDate(board.getRegDate())
                .build();

        return boardOutput;
    }

    @Override
    public void modify(BoardOutput boardOutput) {

        Optional<Board> OptionalBoard = boardRepository.findById(boardOutput.getId());
        Board board = OptionalBoard.orElseThrow();

        Optional<Category> OptionalCategory = categoryRepository.findByName(boardOutput.getCategory());
        Category category = OptionalCategory.orElseThrow();

        board.setTitle(boardOutput.getTitle());
        board.setContents(boardOutput.getContents());
        board.setCategory(category);
        board.setUpdateDate(LocalDateTime.now());

        boardRepository.save(board);

    }

    @Override
    public void remove(Long bno) {

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        board.setDeleted(true);
        board.setDeletedDate(LocalDateTime.now());

        boardRepository.save(board);
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
    public ServiceResult setBoardLike(Long id, String email, LikeStatus status) {

        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            return ServiceResult.fail("글이 존재하지 않습니다.");
        }
        Board board = optionalBoard.get();

        Optional<Member> optionalUser = memberRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        Member member = optionalUser.get();

        Optional<BoardLike> existingLike = boardLikeRepository.findByBoardAndMember(board, member);

        if (existingLike.isPresent()) {
            // 사용자가 이미 좋아요/싫어요를 한 경우 상태 변경 또는 취소
            BoardLike boardLike = existingLike.get();
            if (status == LikeStatus.NONE) {
                // 상태를 취소하는 경우
                boardLikeRepository.delete(boardLike);
            } else {
                // 상태를 변경하는 경우
                boardLike.setLikeStatus(status);
                boardLikeRepository.save(boardLike);
            }
        } else if (status != LikeStatus.NONE) {
            // 새로운 좋아요/싫어요를 추가하는 경우
            boardLikeRepository.save(BoardLike.builder()
                    .member(member)
                    .board(board)
                    .regDate(LocalDateTime.now())
                    .likeStatus(status)
                    .build());
        }

        return ServiceResult.success();
    }

    @Override
    public ServiceResult<Page<Board>> getPopularBoardsToday(Pageable pageable) {
        LocalDate today = LocalDate.now();
        Page<Board> popularBoards = boardRepository.findPopularBoardsByDate(today, pageable);

        if (popularBoards.isEmpty()) {
            return ServiceResult.fail("오늘의 인기 게시글이 없습니다.");
        }
        return ServiceResult.success(popularBoards);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Board getPost(long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("failed to load post : cannot find post id"));
    }

    @Override
    public PageResponseDto<Board> getPostList(PageRequestDto pageRequestDto) {
        String[] types = pageRequestDto.getTypes();
        String keyword = pageRequestDto.getKeyword();
        Pageable pageable = pageRequestDto.getPageable("id");

        Page<Board> result = boardRepository.searchAll(pageRequestDto.getCno(), types, keyword, pageable);
        //Page<Board> result = boardRepository.findByCategory(pageRequestDto.getCno(), pageable);

        List<Board> dtoList = result.getContent();

        return PageResponseDto.<Board>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public PageResponseDto<Board> getFavoriteList(PageRequestDto pageRequestDto, List<MemberCategoryDto> favoriteList) {
        String[] types = pageRequestDto.getTypes();
        String keyword = pageRequestDto.getKeyword();
        Pageable pageable = pageRequestDto.getPageable("id");
        List<Long> cnoList = new ArrayList<>();
        for (int i = 0; i < favoriteList.size(); i++) {
            String name = favoriteList.get(i).getName();
            cnoList.add(categoryRepository.findByName(name).get().getCno());
        }

        Page<Board> result = boardRepository.searchFavorite(cnoList, types, keyword, pageable);

        List<Board> dtoList = result.getContent();

        return PageResponseDto.<Board>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();

    }



}
