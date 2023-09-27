package com.sw300.community.model;


import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

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
