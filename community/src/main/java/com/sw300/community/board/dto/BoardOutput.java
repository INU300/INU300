package com.sw300.community.board.dto;

import java.time.LocalDateTime;
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
public class BoardOutput {

    private Long id;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String title;

    @NotEmpty
    private  String contents;

    private String category;

    private String member;

    private LocalDateTime regDate;

}
