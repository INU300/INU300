package com.sw300.community.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

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

    private String school;

    private String department;

    private String subclass;

    @Builder
    public Member(String email, String password,String name, String school, String department, String subclass){
        this.email = email;
        this.password = password;
        this.name = name;
        this.school = school;
        this.department = department;
        this.subclass = subclass;
    }

}
