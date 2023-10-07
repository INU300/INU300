package com.sw300.community.service;

import com.sw300.community.dto.PageRequestDto;
import com.sw300.community.dto.PageResponseDto;
import com.sw300.community.model.Board;
import com.sw300.community.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.stream.IntStream;

@SpringBootTest
//@Transactional
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    @Commit
    public void testWritePost() {

        Long cno = 3L;

        Category category = Category.builder().cno(cno).build();

        IntStream.rangeClosed(1,100).forEach(i -> {
            Board board = Board.builder()
                    .title("썰게시판 제목" + i)
                    .content("썰게시판 내용" + i)
                    .category(category)
                    .build();
            boardService.writePost(board);
        });

        //System.out.println("testWritePost: " + board);
    }

    @Test
    public void testGetPost() {

        Board board = boardService.getPost(1L);

        System.out.println("testGetPost: " + board);
    }

    @Test
    public void testGetPostList() {

        Board board = Board.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .build();
        boardService.writePost(board);

        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDto<Board> responseDto = boardService.getPostList(pageRequestDto);

        //List<Board> boardList = responseDto.getDtoList();

        System.out.println(responseDto);
    }
}
