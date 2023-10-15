package com.sw300.community.category.dto;

import lombok.*;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long cno;

    @NotEmpty
    private String name;

    private int dailyVisitors;
}