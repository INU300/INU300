package com.sw300.community.repository;

import com.sw300.community.dto.PageRequestDto;
import com.sw300.community.model.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testFindByCategory() {
        PageRequestDto pageRequestDto = PageRequestDto.builder().cno(1L).build();
        Pageable pageable = pageRequestDto.getPageable("id");

        Page<Board> result = boardRepository.findByCategory(pageRequestDto.getCno(), pageable);

        List<Board> dtoList = result.getContent();

        System.out.println(dtoList.get(0));
    }
}