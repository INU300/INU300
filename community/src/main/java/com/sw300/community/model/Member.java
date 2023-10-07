package com.sw300.community.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @NotNull
    private String password;
    @NotNull
    private String name;

    @NotNull
    private String nickname;

    private String school;// 학교

    private String department;// 학과

    private String subclass;// 소분류

    private int upVotes;   // 추천 수
    private int downVotes; // 비추천 수


    @ElementCollection(fetch =  FetchType.LAZY)
    private List<String> bookmark;

    @Builder
    public Member(String email, String password,String name, String nickname, String school, String department, String subclass, List<String> bookmark){
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.school = school;
        this.department = department;
        this.subclass = subclass;
        this.bookmark = bookmark;
    }

    public void changeNickname(String newNickname){
        this.nickname = newNickname;
    }
    public void changePassword(String password){this.password = password;}

}
