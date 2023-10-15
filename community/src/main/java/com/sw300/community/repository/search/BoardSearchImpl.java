package com.sw300.community.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.sw300.community.board.model.Board;
import com.sw300.community.model.QBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {


    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> searchAll(Long cno, String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if ( (types != null && types.length > 0 ) && keyword != null ) {  // 검색 조건과 키워드가 있다면

            BooleanBuilder booleanBuilder = new BooleanBuilder();  // 괄호 (

            for (String type : types) {

                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        //booleanBuilder.or(board.member.name.contains(keyword));
                        break;
                }
            }  // end for
            query.where(booleanBuilder);
        }  // end if

        // id > 0
        query.where(board.category.cno.eq(cno));

        // paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<Board> searchFavorite(List<Long> cnoList, String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if ( (types != null && types.length > 0 ) && keyword != null ) {  // 검색 조건과 키워드가 있다면

            BooleanBuilder booleanBuilder = new BooleanBuilder();  // 괄호 (

            for (String type : types) {

                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        //booleanBuilder.or(board.member.name.contains(keyword));
                        break;
                }
            }  // end for
            query.where(booleanBuilder);
        }  // end if

        BooleanBuilder booleanBuilder = new BooleanBuilder();  // 괄호 (

        booleanBuilder.or(board.category.cno.eq(cnoList.get(0)));
        booleanBuilder.or(board.category.cno.eq(cnoList.get(1)));
        booleanBuilder.or(board.category.cno.eq(cnoList.get(2)));
        booleanBuilder.or(board.category.cno.eq(cnoList.get(3)));
        booleanBuilder.or(board.category.cno.eq(cnoList.get(4)));

        query.where(booleanBuilder);

        // paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }
}
