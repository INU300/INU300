package com.sw300.community.repository;

import com.sw300.community.board.model.Board;
import com.sw300.community.board.repository.BoardRepository;
import com.sw300.community.category.model.Category;
import com.sw300.community.category.repository.CategoryRepository;
import com.sw300.community.common.dto.PageRequestDto;
import com.sw300.community.member.model.Member;
import com.sw300.community.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;


@SpringBootTest
class BoardRepositoryTest {

    @Autowired private BoardRepository boardRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    public void testFindByCategory() {
        PageRequestDto pageRequestDto = PageRequestDto.builder().cno(1L).build();
        Pageable pageable = pageRequestDto.getPageable("id");

        Page<Board> result = boardRepository.findByCategory(pageRequestDto.getCno(), pageable);

        List<Board> dtoList = result.getContent();

        System.out.println(dtoList.get(0));
    }

    @Test
    public void testSearchAll() {

        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Page<Board> result = boardRepository.searchAll(1L, types, keyword, pageable);
    }

    @Test
    public void testSearchAll2() {

        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Page<Board> result = boardRepository.searchAll(1L, types, keyword, pageable);

        //total pages
        System.out.println(result.getTotalPages());

        //page size
        System.out.println(result.getSize());

        //pageNumber
        System.out.println(result.getNumber());

        //prev next
        System.out.println(result.hasPrevious() +": " + result.hasNext());

        result.getContent().forEach(board -> System.out.println(board));
    }

    @Test
    public void testInsert() {
        Optional<Category> category = categoryRepository.findById(1L);
        Optional<Member> member = memberRepository.findByEmail("asdfg@naver.com");

        IntStream.rangeClosed(1,100).forEach(i -> {

            Board board = Board.builder()
                    .title("title..." + i)
                    .contents("content..." + i)
                    .category(category.get())
                    .member(member.get())
                    .regDate(LocalDateTime.now())
                    .build();

            boardRepository.save(board);
        });
    }
}