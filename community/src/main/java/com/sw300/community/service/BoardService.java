package com.sw300.community.service;

import com.sw300.community.dto.*;
import com.sw300.community.model.Board;
import com.sw300.community.model.Reply;
import com.sw300.community.repository.BoardRepository;
import com.sw300.community.repository.CategoryRepository;
import com.sw300.community.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {


    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    private final CategoryRepository categoryRepository;

    @Transactional
    public void writePost(Board board) {
        boardRepository.save(board);
    }

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
                .total((int)result.getTotalElements())
                .build();
    }


    @Transactional
    public PageResponseDto<Board> getFavoriteList(PageRequestDto pageRequestDto,List<MemberCategoryDto> favoriteList){
        String[] types = pageRequestDto.getTypes();
        String keyword = pageRequestDto.getKeyword();
        Pageable pageable = pageRequestDto.getPageable("id");
        List<Long> cnoList = new ArrayList<>();
        for(int i = 0 ; i<5;i++){
            String name = favoriteList.get(i).getName();
            cnoList.add(categoryRepository.findByName(name).get().getCno());
        }

        Page<Board> result = boardRepository.searchFavorite(cnoList, types, keyword, pageable);

        List<Board> dtoList = result.getContent();

        return PageResponseDto.<Board>withAll()
                .pageRequestDto(pageRequestDto)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();

    }

    @Transactional(readOnly = true)
    public Board getPost(long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("failed to load post : cannot find post id"));
    }

    @Transactional
    public void deletePost(long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public void updatePost(long id, Board requestBoard) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("failed to load post : cannot post id"));
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
    }

    @Transactional
    public ResponseDto<Integer> vote(Long boardId, String voteType) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("failed to load post : cannot find post id"));

        if ("upVote".equals(voteType)) {
            board.setUpVotes(board.getUpVotes() + 1);
        } else if ("downVote".equals(voteType)) {
            board.setDownVotes(board.getDownVotes() + 1);
        } else {
            return new ResponseDto<>(HttpStatus.BAD_REQUEST, -1);  // Unsupported vote type
        }

        boardRepository.save(board);
        return new ResponseDto<>(HttpStatus.OK, 1);  // Success
    }

    @Transactional
    public void writeReply(ReplySaveRequestDto replyDto) {
        Board board = boardRepository.findById(replyDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Failed to write reply : cannot find post id")); // 영속화

//        User user = userRepository.findById(replyDto.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("Failed to write reply : cannot find user id")); // 영속화

        Reply reply = Reply.builder()
//                .user(user)
                .board(board)
                .content(replyDto.getContent())
                .build();

        replyRepository.save(reply);
    }

    public Page<Reply> getRepliesByBoardId(Long boardId, Pageable pageable) {
        return replyRepository.findByBoardId(boardId, pageable);
    }

    @Transactional
    public void deleteReply(long replyId) {
        replyRepository.deleteById(replyId);
    }
}
