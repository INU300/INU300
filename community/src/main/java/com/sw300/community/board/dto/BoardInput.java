package com.sw300.community.board.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardInput {

    @NotEmpty
    @Size(min = 3, max = 50)
    private String title;

    @NotEmpty
    private  String contents;

    private String category;

    private String member;

}
