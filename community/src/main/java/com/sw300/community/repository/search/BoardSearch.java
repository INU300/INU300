package com.sw300.community.repository.search;

import com.sw300.community.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    Page<Board> searchAll(Long cno, String[] types, String keyword, Pageable pageable);
}
