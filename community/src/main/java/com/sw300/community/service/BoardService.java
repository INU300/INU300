package com.sw300.community.service;

import com.sw300.community.dto.ReplySaveRequestDto;
import com.sw300.community.model.Board;
import com.sw300.community.model.Reply;
import com.sw300.community.repository.BoardRepository;
import com.sw300.community.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    public BoardService(BoardRepository boardRepository, ReplyRepository replyRepository) {
        this.boardRepository = boardRepository;
        this.replyRepository = replyRepository;
    }

    @Transactional
    public void writePost(Board board) {
        boardRepository.save(board);
    }

    public Page<Board> getPostList(Pageable pageable) {
        return boardRepository.findAll(pageable);
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
