package com.sw300.community.repository.search;

import com.sw300.community.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardSearch {

    Page<Board> searchAll(Long cno, String[] types, String keyword, Pageable pageable);

    Page<Board> searchFavorite(List<Long> cnoList, String[] types, String keyword, Pageable pageable);
}
