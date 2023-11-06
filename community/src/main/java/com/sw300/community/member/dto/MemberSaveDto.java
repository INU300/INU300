package com.sw300.community.member.dto;


import com.sw300.community.member.model.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemberSaveDto {

    private String email;
    private String password;
    private String name;
    private String nickname;
    private String school;
    private String department;
    private String subclass;
    private List<String> categories;

    @Builder
    public MemberSaveDto(String email, String password, String name, String nickname ,String school, String department, List<String> categories){
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.school = school;
        this.department = department;
        this.categories = categories;
    }

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .school(school)
                .department(department)
                .subclass(subclass)
                .build();
    }
}
