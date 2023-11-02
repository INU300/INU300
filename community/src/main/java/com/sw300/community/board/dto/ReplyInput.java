package com.sw300.community.board.dto;

import com.sw300.community.board.model.Board;
import com.sw300.community.category.model.Category;
import com.sw300.community.member.model.Member;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReplyInput {

    private Long boardId;;

    @NotBlank(message = "댓글 내용은 필수 항목입니다.")
    private  String contents;

}
