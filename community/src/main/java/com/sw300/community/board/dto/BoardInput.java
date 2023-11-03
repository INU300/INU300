package com.sw300.community.board.dto;

import com.sw300.community.category.model.Category;
import com.sw300.community.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardInput {

    private Member member;

    private Category category;

    @NotBlank(message = "게시판 제목은 필수 항목입니다.")
    private String title;

    private  String contents;

}
