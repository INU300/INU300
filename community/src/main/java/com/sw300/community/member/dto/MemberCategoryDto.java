package com.sw300.community.member.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberCategoryDto {

    private String name;
    private int viewCount;

    @Builder
    public MemberCategoryDto(String name,int viewCount){
        this.name =name;
        this.viewCount=viewCount;
    }
}
