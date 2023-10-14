package com.sw300.community.dto;


import com.sw300.community.model.MemberCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemberInformationDto {
    private String email;
    private String name;
    private String nickname;
    private String school;
    private String department;
    private String subclass;
    private List<MemberCategory> favorite;

    @Builder
    public  MemberInformationDto(String email, String name, String nickname, String school, String department, String subclass, List<MemberCategory> favorite){
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.school = school;
        this.department = department;
        this.subclass = subclass;
        this.favorite = favorite;
    }
}
