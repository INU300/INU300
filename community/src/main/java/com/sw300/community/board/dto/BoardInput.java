package com.sw300.community.board.dto;

import com.sw300.community.model.Category;
import com.sw300.community.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardInput {

    @NotBlank(message = "게시판 제목은 필수 항목입니다.")
    private Member member;

    private Category category;

    private String title;

    private  String contents;

}
