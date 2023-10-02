package com.sw300.community.service;

import com.sw300.community.model.Board;
import com.sw300.community.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
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
}
