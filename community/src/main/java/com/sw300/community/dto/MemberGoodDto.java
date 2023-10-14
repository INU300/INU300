package com.sw300.community.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberGoodDto {
    private String nickname;
    private int upVotes;   // 추천 수

    @Builder
    public MemberGoodDto(String nickname,int upVotes){
        this.nickname = nickname;
        this.upVotes = upVotes;
    }
}
