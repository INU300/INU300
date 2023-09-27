package com.sw300.community.dto;


import com.sw300.community.model.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberSaveDto {

    private String email;
    private String password;
    private String name;
    private String school;
    private String department;
    private String subclass;

    @Builder
    public MemberSaveDto(String email, String password, String name, String school, String department){
        this.email = email;
        this.password = password;
        this.name = name;
        this.school = school;
        this.department = department;
    }

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .name(password)
                .name(name)
                .school(school)
                .department(department)
                .subclass(subclass)
                .build();
    }
}
